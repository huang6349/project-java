package org.huangyalong.modules.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.Getter;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.huangyalong.modules.ai.domain.AiChunk;
import org.huangyalong.modules.ai.domain.AiDocument;
import org.huangyalong.modules.ai.mapper.AiDocumentMapper;
import org.huangyalong.modules.ai.request.AiDocumentBO;
import org.huangyalong.modules.ai.service.AiChunkService;
import org.huangyalong.modules.ai.service.AiDocumentService;
import org.huangyalong.modules.file.domain.File;
import org.huangyalong.modules.file.service.FileService;
import org.myframework.ai.core.AiBot;
import org.myframework.core.exception.BusinessException;
import org.myframework.core.util.ReactorUtil;
import org.noear.solon.ai.rag.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.Serializable;
import java.util.List;

import static cn.dev33.satoken.util.StrFormatter.format;
import static cn.hutool.core.convert.Convert.toLong;
import static org.huangyalong.modules.ai.domain.AiChunkExtras.NAME_ORIG_ID;
import static org.huangyalong.modules.ai.domain.table.AiChunkTableDef.AI_CHUNK;
import static org.huangyalong.modules.ai.domain.table.AiDocumentTableDef.AI_DOCUMENT;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;
import static org.myframework.core.mybatisflex.JsonMethods.ue;
import static org.myframework.core.util.ServiceUtil.randomCode;

@Getter
@Service
public class AiDocumentServiceImpl extends ReactorServiceImpl<AiDocumentMapper, AiDocument> implements AiDocumentService {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AiChunkService chunkService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(AiDocumentBO documentBO) {
        validateAddOrUpdate(documentBO);
        var file = Opt.ofNullable(documentBO)
                .map(AiDocumentBO::getFilename)
                .map(getFileService()::getByFilename)
                .map(ReactorUtil::runBlock)
                .orElseThrow(() -> new BusinessException("文件不存在"));
        var data = AiDocument.create()
                .setCode(randomCode())
                .with(documentBO)
                .with(file);
        return save(data)
                .thenReturn(Tuples.of(data, file))
                .flatMap(this::textSplit)
                .thenReturn(Boolean.TRUE);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(AiDocumentBO documentBO) {
        validateAddOrUpdate(documentBO);
        var id = Opt.ofNullable(documentBO)
                .map(AiDocumentBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(documentBO);
        return updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data)
                .thenReturn(id)
                .map(documentId -> AiChunk.create()
                        .select(ue(AI_CHUNK.EXTRAS, NAME_ORIG_ID))
                        .where(AI_CHUNK.DOCUMENT_ID.eq(documentId))
                        .listAs(String.class))
                .doOnNext(AiBot.getInstance()::delete)
                .thenReturn(id)
                .map(documentId -> getChunkService()
                        .queryChain()
                        .where(AI_CHUNK.DOCUMENT_ID.eq(documentId)))
                .doOnNext(getChunkService()::remove)
                .thenReturn(Boolean.TRUE);
    }

    void validateAddOrUpdate(AiDocumentBO documentBO) {
        var name = Opt.ofNullable(documentBO)
                .map(AiDocumentBO::getName)
                .get();
        var id = Opt.ofNullable(documentBO)
                .map(AiDocumentBO::getId)
                .get();
        var exists = queryChain()
                .where(AI_DOCUMENT.NAME.eq(name))
                .and(AI_DOCUMENT.NAME.isNotNull())
                .and(AI_DOCUMENT.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("名称已存在");
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }

    Mono<Tuple2<AiDocument, File>> textSplit(Tuple2<AiDocument, File> tuple2) {
        return Mono.justOrEmpty(tuple2).flatMap((Tuple2<AiDocument, File> objects) -> {
            var document = Opt.ofNullable(objects)
                    .map(Tuple2::getT1)
                    .orElseThrow(() -> new BusinessException("文档不能为空"));
            var file = Opt.ofNullable(objects)
                    .map(Tuple2::getT2)
                    .map(File::without)
                    .orElseThrow(() -> new BusinessException("文件不能为空"));
            var documentId = Opt.ofNullable(document)
                    .map(AiDocument::getId)
                    .get();
            return Mono.just(Tuples.of(document, file, documentId));
        }).flatMap((Tuple3<AiDocument, FileInfo, Long> objects) -> {
            var documentId = Opt.ofNullable(objects)
                    .map(Tuple3::getT3)
                    .get();
            var file = Opt.ofNullable(objects)
                    .map(Tuple3::getT2)
                    .get();
            var ext = format(".{}", file.getExt());
            var tempFile = FileUtil.createTempFile(ext, Boolean.TRUE);
            try {
                var downloader = getStorageService()
                        .download(file);
                downloader.file(tempFile);
                if (FileUtil.isEmpty(tempFile))
                    return Mono.error(new BusinessException("文件下载失败"));
                var documents = Opt.ofNullable(tempFile)
                        .map(AiBot.getInstance()::insert)
                        .orElseGet(CollUtil::newArrayList);
                // 使用完毕后删除临时文件
                FileUtil.del(tempFile);
                return Mono.just(Tuples.of(documents, documentId));
            } catch (Exception e) {
                StaticLog.error(e);
                FileUtil.del(tempFile); // 发生异常时也删除临时文件
                return Mono.error(new BusinessException("文件处理失败"));
            }
        }).flatMap((Tuple2<List<Document>, Long> objects) -> {
            var documentId = Opt.ofNullable(objects)
                    .map(Tuple2::getT2)
                    .get();
            var documents = Opt.ofNullable(objects)
                    .map(Tuple2::getT1)
                    .get();
            if (ObjectUtil.isEmpty(documents))
                return Mono.error(new BusinessException("文件分片失败"));
            var chunks = CollUtil.<AiChunk>newArrayList();
            for (var document : documents)
                chunks.add(AiChunk.create()
                        .setDocumentId(documentId)
                        .setSort(chunks.size())
                        .with(document));
            return Mono.just(getChunkService()
                    .getBlockService()
                    .saveBatch(chunks));
        }).thenReturn(tuple2);
    }
}

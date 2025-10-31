package org.huangyalong.modules.file.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.Getter;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.huangyalong.modules.file.domain.File;
import org.huangyalong.modules.file.domain.FilePart;
import org.huangyalong.modules.file.service.FilePartService;
import org.huangyalong.modules.file.service.FileService;
import org.myframework.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static org.huangyalong.modules.file.domain.table.FilePartTableDef.FILE_PART;
import static org.huangyalong.modules.file.domain.table.FileTableDef.FILE;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@Getter
@Service
public class FileRecorderImpl implements FileRecorder {

    @Autowired
    private FilePartService partService;

    @Autowired
    private FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public boolean save(FileInfo fileInfo) {
        var data = File.create()
                .with(fileInfo);
        return runBlock(getFileService()
                .save(data)
                .thenReturn(data)
                .map(File::getId)
                .map(Convert::toStr)
                .doOnNext(fileInfo::setId)
                .thenReturn(Boolean.TRUE));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(FileInfo fileInfo) {
        var id = Opt.ofNullable(fileInfo)
                .map(FileInfo::getId)
                .get();
        var data = getFileService()
                .getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(fileInfo);
        runBlock(getFileService()
                .updateById(data));
    }

    @Override
    public FileInfo getByUrl(String url) {
        var query = QueryWrapper.create()
                .where(FILE.URL.eq(url));
        return runBlock(getFileService()
                .getOne(query)
                .map(File::without));
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String url) {
        var query = QueryWrapper.create()
                .where(FILE.URL.eq(url));
        return runBlock(getFileService()
                .remove(query));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFilePart(FilePartInfo partInfo) {
        var data = FilePart.create()
                .with(partInfo);
        runBlock(getPartService()
                .save(data)
                .thenReturn(data)
                .map(FilePart::getId)
                .map(Convert::toStr)
                .doOnNext(partInfo::setId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFilePartByUploadId(String uploadId) {
        var query = QueryWrapper.create()
                .where(FILE_PART.UPLOAD_ID.eq(uploadId));
        runBlock(getPartService()
                .remove(query));
    }
}

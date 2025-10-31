package org.huangyalong.modules.ai.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.ai.domain.AiDocument;
import org.huangyalong.modules.ai.request.AiDocumentBO;
import org.huangyalong.modules.ai.request.AiDocumentQueries;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.ai.domain.AiDocumentExtras.*;
import static org.huangyalong.modules.ai.domain.table.AiDocumentTableDef.AI_DOCUMENT;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface AiDocumentService extends ReactorService<AiDocument> {

    default QueryWrapper getQueryWrapper(AiDocumentQueries queries,
                                         QueryWrapper query) {
        query.where(AI_DOCUMENT.NAME.like(queries.getName(), If::hasText));
        query.where(AI_DOCUMENT.CODE.like(queries.getCode(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(AiDocumentQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(AI_DOCUMENT.ID, Boolean.FALSE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(AI_DOCUMENT.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(AI_DOCUMENT.ID,
                        AI_DOCUMENT.NAME,
                        AI_DOCUMENT.CODE,
                        AI_DOCUMENT.DESC,
                        AI_DOCUMENT.STATUS,
                        AI_DOCUMENT.CREATE_TIME,
                        AI_DOCUMENT.UPDATE_TIME)
                .select(ue(AI_DOCUMENT.EXTRAS, NAME_FILENAME).as(AiDocument::getFilename),
                        ue(AI_DOCUMENT.EXTRAS, NAME_EXT).as(AiDocument::getExt),
                        ue(AI_DOCUMENT.EXTRAS, NAME_CONTENT_TYPE).as(AiDocument::getContentType))
                .from(AI_DOCUMENT);
    }

    @Override
    default Mono<AiDocument> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(AiDocumentBO documentBO);

    Mono<Boolean> update(AiDocumentBO documentBO);

    Mono<Boolean> delete(Serializable id);
}

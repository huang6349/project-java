package org.huangyalong.modules.ai.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.ai.domain.AiChunk;
import org.huangyalong.modules.ai.request.AiChunkQueries;

import static org.huangyalong.modules.ai.domain.table.AiChunkTableDef.AI_CHUNK;

public interface AiChunkService extends ReactorService<AiChunk> {

    default QueryWrapper getQueryWrapper(AiChunkQueries queries,
                                         QueryWrapper query) {
        query.where(AI_CHUNK.DOCUMENT_ID.eq(queries.getDocumentId()));
        query.where(AI_CHUNK.DOCUMENT_ID.isNotNull());
        return query;
    }

    default QueryWrapper getQueryWrapper(AiChunkQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(AI_CHUNK.ID, Boolean.FALSE);
        return getQueryWrapper(queries, query);
    }
}

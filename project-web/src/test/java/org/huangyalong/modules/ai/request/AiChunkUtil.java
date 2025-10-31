package org.huangyalong.modules.ai.request;

import cn.hutool.core.lang.Opt;
import org.huangyalong.modules.ai.domain.AiChunk;

import static org.huangyalong.modules.ai.domain.table.AiChunkTableDef.AI_CHUNK;

public interface AiChunkUtil {

    static AiChunk getEntity() {
        return AiChunk.create()
                .orderBy(AI_CHUNK.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(AiChunk::getId)
                .get();
    }
}

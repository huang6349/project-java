package org.huangyalong.modules.ai.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.ai.domain.AiChunk;
import org.huangyalong.modules.ai.request.AiChunkQueries;
import org.huangyalong.modules.ai.service.AiChunkService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.base.web.curd.QueryController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuth(replace = "@ai-document")
@RestController
@RequestMapping("/ai/document/chunk")
@Tag(name = "文档分片")
public class AiChunkController extends SuperSimpleController<
        AiChunkService,
        AiChunk
        > implements QueryController<
        AiChunk,
        Long,
        AiChunkQueries> {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(AiChunkQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }
}

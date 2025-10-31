package org.huangyalong.modules.ai.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.ai.domain.AiDocument;
import org.huangyalong.modules.ai.request.AiDocumentBO;
import org.huangyalong.modules.ai.request.AiDocumentQueries;
import org.huangyalong.modules.ai.service.AiDocumentService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@ai-document")
@RestController
@RequestMapping("/ai/document")
@Tag(name = "文档管理")
public class AiDocumentController extends ReactorController<
        AiDocumentService,
        Long,
        AiDocument,
        AiDocumentQueries,
        AiDocumentBO,
        AiDocumentBO> {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(AiDocumentQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(AiDocumentBO documentBO) {
        var data = getBaseService()
                .add(documentBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(AiDocumentBO documentBO) {
        var data = getBaseService()
                .update(documentBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

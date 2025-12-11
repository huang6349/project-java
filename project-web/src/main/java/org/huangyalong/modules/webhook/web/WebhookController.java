package org.huangyalong.modules.webhook.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.webhook.domain.Webhook;
import org.huangyalong.modules.webhook.request.WebhookBO;
import org.huangyalong.modules.webhook.request.WebhookQueries;
import org.huangyalong.modules.webhook.service.WebhookService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@webhook")
@RestController
@RequestMapping("/webhook")
@Tag(name = "网络钩子")
public class WebhookController extends ReactorController<
        WebhookService,
        Long,
        Webhook,
        WebhookQueries,
        WebhookBO,
        WebhookBO> {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(WebhookQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(WebhookBO webhookBO) {
        var data = getBaseService()
                .add(webhookBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(WebhookBO webhookBO) {
        var data = getBaseService()
                .update(webhookBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }

    @PreCheckPermission(value = {"{}:add", "{}:save"}, mode = PreMode.OR)
    @PostMapping("/{id:.+}/_test")
    @Operation(summary = "推送模拟事件")
    public Mono<Boolean> test(@PathVariable Long id) {
        return getBaseService()
                .test(id);
    }
}

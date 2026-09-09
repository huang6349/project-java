package org.huangyalong.modules.notify.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.request.AppBO;
import org.huangyalong.modules.notify.request.AppQueries;
import org.huangyalong.modules.notify.service.NotifyAppService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@notify")
@RestController
@RequestMapping("/notify/app")
@Tag(name = "消息应用")
public class NotifyAppController extends ReactorController<
        NotifyAppService,
        Long,
        NotifyApp,
        AppQueries,
        AppBO,
        AppBO> {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(AppQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(AppBO appBO) {
        var data = getBaseService()
                .add(appBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(AppBO appBO) {
        var data = getBaseService()
                .update(appBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

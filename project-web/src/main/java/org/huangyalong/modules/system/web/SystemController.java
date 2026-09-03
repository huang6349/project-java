package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.request.SystemBO;
import org.huangyalong.modules.system.service.SystemService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.base.web.curd.UpdateController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@PreAuth(replace = "@system")
@RestController
@RequestMapping("/system/configs")
@Tag(name = "系统配置")
public class SystemController extends SuperSimpleController<
        SystemService,
        System>
        implements UpdateController<
        System,
        SystemBO> {

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(SystemBO systemBO) {
        var data = getBaseService()
                .update(systemBO);
        return ApiResponse.ok(data);
    }

    @GetMapping
    @Operation(summary = "获取配置信息")
    public Mono<Map<String, Object>> configs() {
        return getBaseService()
                .configs();
    }
}

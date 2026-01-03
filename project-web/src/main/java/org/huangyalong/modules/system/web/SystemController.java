package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.service.SystemService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@PreAuth(replace = "@system")
@RestController
@RequestMapping("/system")
@Tag(name = "系统管理")
public class SystemController extends SuperSimpleController<SystemService, System> {

    @Operation(summary = "获取配置信息")
    @GetMapping("/configs")
    public Mono<Map<String, Object>> configs() {
        return getBaseService()
                .configs();
    }
}

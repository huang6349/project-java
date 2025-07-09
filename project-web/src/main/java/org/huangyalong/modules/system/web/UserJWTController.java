package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.LoginBO;
import org.huangyalong.modules.system.response.JWTToken;
import org.huangyalong.modules.system.service.UserJWTService;
import org.myframework.base.web.SuperSimpleController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@Tag(name = "用户管理")
public class UserJWTController extends SuperSimpleController<UserJWTService, User> {

    @PostMapping("/authenticate")
    @Operation(summary = "获取授权令牌")
    public Mono<JWTToken> authorize(@RequestBody @Validated LoginBO loginBO) {
        return getBaseService()
                .authorize(loginBO);
    }
}

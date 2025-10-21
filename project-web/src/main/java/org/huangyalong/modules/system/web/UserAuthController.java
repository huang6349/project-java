package org.huangyalong.modules.system.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.response.Authentication;
import org.huangyalong.modules.system.service.UserAuthService;
import org.myframework.base.web.SuperSimpleController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserAuthController extends SuperSimpleController<UserAuthService, User> {

    @SaCheckLogin
    @GetMapping("/me")
    @Operation(summary = "获取授权信息")
    public Mono<Authentication> me() {
        return getBaseService()
                .me();
    }
}

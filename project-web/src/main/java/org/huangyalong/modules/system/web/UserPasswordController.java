package org.huangyalong.modules.system.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.UserPasswordBO;
import org.huangyalong.modules.system.service.UserPasswordService;
import org.myframework.base.validation.Update;
import org.myframework.base.web.reactive.ReactiveController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserPasswordController extends ReactiveController<UserPasswordService, User> {

    @SaCheckLogin
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Mono<Boolean> update(@RequestBody @Validated(Update.class) UserPasswordBO userPasswordBO) {
        return getReactorService()
                .update(userPasswordBO);
    }
}

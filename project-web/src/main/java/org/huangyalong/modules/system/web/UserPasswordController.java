package org.huangyalong.modules.system.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.PasswordBO;
import org.huangyalong.modules.system.service.UserPasswordService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserPasswordController extends SuperSimpleController<UserPasswordService, User> {

    @PreCheckPermission(value = "{}:update")
    @PutMapping("/{id}/_reset")
    @Operation(summary = "重置密码")
    public Mono<Boolean> reset(@PathVariable Long id) {
        return getBaseService()
                .reset(id);
    }

    @SaCheckLogin
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Mono<Boolean> update(@RequestBody @Validated PasswordBO passwordBO) {
        return getBaseService()
                .update(passwordBO);
    }
}

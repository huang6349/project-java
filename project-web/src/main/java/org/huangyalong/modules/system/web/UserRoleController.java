package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user/role")
@Tag(name = "用户角色")
public class UserRoleController extends SuperSimpleController<UserRoleService, Role> {

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改")
    public Mono<Boolean> assoc(@RequestBody @Validated UserRoleBO roleBO) {
        return getBaseService()
                .assoc(roleBO);
    }
}

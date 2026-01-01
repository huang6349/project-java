package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.request.UserRoleQueries;
import org.huangyalong.modules.system.response.UserRoleVO;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Getter
@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user/role")
@Tag(name = "用户角色")
public class UserRoleController extends SuperSimpleController<UserRoleService, Role> {

    @Autowired
    private TenantProperties properties;

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping
    @Operation(summary = "根据主键查询")
    public Mono<UserRoleVO> query(UserRoleQueries queries) {
        return getBaseService()
                .query(queries);
    }

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改单个数据")
    public Mono<Boolean> update(@RequestBody @Validated UserRoleBO roleBO) {
        return getBaseService()
                .assoc(roleBO);
    }
}

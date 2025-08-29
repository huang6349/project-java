package org.huangyalong.modules.system.web;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.core.satoken.helper.RoleHelper;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.request.UserRoleQueries;
import org.huangyalong.modules.system.response.UserRoleVO;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user/role")
@Tag(name = "用户角色")
public class UserRoleController extends SuperSimpleController<UserRoleService, Role> {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping
    @Operation(summary = "单体查询")
    public Mono<UserRoleVO> query(@Validated UserRoleQueries queries) {
        var tenantId = Opt.ofNullable(queries)
                .map(UserRoleQueries::getTenantId)
                .get();
        var id = Opt.ofNullable(queries)
                .map(UserRoleQueries::getId)
                .get();
        var roleVO = new UserRoleVO();
        roleVO.setTenantId(tenantId);
        roleVO.setId(id);
        roleVO.setRoleIds(RoleHelper.fetch(tenantId, id));
        return Mono.just(roleVO);
    }

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改")
    public Mono<Boolean> update(@RequestBody @Validated UserRoleBO roleBO) {
        return getBaseService()
                .assoc(roleBO);
    }
}

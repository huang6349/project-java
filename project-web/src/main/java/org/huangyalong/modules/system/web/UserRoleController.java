package org.huangyalong.modules.system.web;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.huangyalong.core.satoken.helper.RoleHelper;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.request.UserRoleQueries;
import org.huangyalong.modules.system.response.UserRoleVO;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.exception.BusinessException;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.core.constants.TenantConstants.INVALID;

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
        var tenantId = getTenantId(queries);
        var id = Opt.ofNullable(queries)
                .map(UserRoleQueries::getId)
                .orElseThrow(() -> new BusinessException("主键不能为空"));
        var roleVO = new UserRoleVO();
        roleVO.setTenantId(tenantId);
        roleVO.setId(id);
        roleVO.setRoleIds(RoleHelper.fetch(tenantId, id));
        return Mono.just(roleVO);
    }

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改单个数据")
    public Mono<Boolean> update(@RequestBody @Validated UserRoleBO roleBO) {
        return getBaseService()
                .assoc(roleBO);
    }

    Long getTenantId(UserRoleQueries queries) {
        var enabled = ofNullable(getProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        if (enabled) {
            return ofNullable(queries)
                    .map(UserRoleQueries::getTenantId)
                    .orElseThrow(() -> new BusinessException("租户不能为空"));
        } else return INVALID;
    }
}

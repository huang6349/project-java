package org.huangyalong.modules.system.web;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.request.UserPermQueries;
import org.huangyalong.modules.system.response.UserPermVO;
import org.huangyalong.modules.system.service.UserPermService;
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
import static org.huangyalong.core.satoken.helper.PermHelper.fetch;

@Getter
@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user/perm")
@Tag(name = "用户权限")
public class UserPermController extends SuperSimpleController<UserPermService, Perm> {

    @Autowired
    private TenantProperties properties;

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping
    @Operation(summary = "根据主键查询（用户权限）")
    public Mono<UserPermVO> query(UserPermQueries queries) {
        var tenantId = getTenantId(queries);
        var id = Opt.ofNullable(queries)
                .map(UserPermQueries::getId)
                .orElseThrow(() -> new BusinessException("主键不能为空"));
        var permVO = new UserPermVO();
        permVO.setTenantId(tenantId);
        permVO.setId(id);
        return getBaseService()
                .list(tenantId, id)
                .collectList()
                .map(permVO::with);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_all")
    @Operation(summary = "根据主键查询（全部权限）")
    public Mono<UserPermVO> all(@Validated UserPermQueries queries) {
        var tenantId = getTenantId(queries);
        var id = Opt.ofNullable(queries)
                .map(UserPermQueries::getId)
                .get();
        var permVO = new UserPermVO();
        permVO.setTenantId(tenantId);
        permVO.setId(id);
        permVO.setPermIds(fetch(tenantId, id));
        return Mono.just(permVO);
    }

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改单个数据")
    public Mono<Boolean> update(@RequestBody @Validated UserPermBO permBO) {
        return getBaseService()
                .assoc(permBO);
    }

    Long getTenantId(UserPermQueries queries) {
        var enabled = ofNullable(getProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        if (enabled) {
            return ofNullable(queries)
                    .map(UserPermQueries::getTenantId)
                    .orElseThrow(() -> new BusinessException("租户不能为空"));
        } else return INVALID;
    }
}

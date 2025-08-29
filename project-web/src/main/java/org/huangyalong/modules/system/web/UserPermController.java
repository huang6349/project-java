package org.huangyalong.modules.system.web;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.request.UserPermQueries;
import org.huangyalong.modules.system.response.UserPermVO;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.huangyalong.core.satoken.helper.PermHelper.fetch;

@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user/perm")
@Tag(name = "用户权限")
public class UserPermController extends SuperSimpleController<UserPermService, Perm> {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping
    @Operation(summary = "单体查询（用户权限）")
    public Mono<UserPermVO> query(@Validated UserPermQueries queries) {
        var tenantId = Opt.ofNullable(queries)
                .map(UserPermQueries::getTenantId)
                .get();
        var id = Opt.ofNullable(queries)
                .map(UserPermQueries::getId)
                .get();
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
    @Operation(summary = "单体查询（全部权限）")
    public Mono<UserPermVO> all(@Validated UserPermQueries queries) {
        var tenantId = Opt.ofNullable(queries)
                .map(UserPermQueries::getTenantId)
                .get();
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
    @Operation(summary = "修改")
    public Mono<Boolean> update(@RequestBody @Validated UserPermBO permBO) {
        return getBaseService()
                .assoc(permBO);
    }
}

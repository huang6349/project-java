package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.RolePermBO;
import org.huangyalong.modules.system.response.RolePermVO;
import org.huangyalong.modules.system.service.RolePermService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@role")
@RestController
@RequestMapping("/role/perm")
@Tag(name = "角色权限")
public class RolePermController extends SuperSimpleController<RolePermService, Perm> {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}")
    @Operation(summary = "单体查询")
    public Mono<RolePermVO> query(@PathVariable Long id) {
        var permVO = new RolePermVO();
        permVO.setId(id);
        return getBaseService()
                .list(id)
                .collectList()
                .map(permVO::with);
    }

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改")
    public Mono<Boolean> update(@RequestBody @Validated RolePermBO permBO) {
        return getBaseService()
                .assoc(permBO);
    }
}

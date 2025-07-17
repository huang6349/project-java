package org.huangyalong.modules.system.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user/perm")
@Tag(name = "用户权限")
public class UserPermController extends SuperSimpleController<UserPermService, Perm> {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}")
    @Operation(summary = "单体查询")
    public Flux<Perm> query(@PathVariable Long id) {
        return getBaseService()
                .list(id);
    }

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PatchMapping
    @Operation(summary = "修改")
    public Mono<Boolean> update(@RequestBody @Validated UserPermBO permBO) {
        return getBaseService()
                .assoc(permBO);
    }
}

package org.huangyalong.modules.system.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.TenantUserBO;
import org.huangyalong.modules.system.request.TenantUserQueries;
import org.huangyalong.modules.system.response.TenantUserVO;
import org.huangyalong.modules.system.service.TenantUserService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@tenant")
@RestController
@RequestMapping("/tenant/user")
@Tag(name = "租户用户")
public class TenantUserController extends ReactorController<
        TenantUserService,
        Long,
        User,
        TenantUserQueries,
        TenantUserBO,
        TenantUserBO> {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(TenantUserQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(TenantUserBO userBO) {
        var data = getBaseService()
                .add(userBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(TenantUserBO userBO) {
        var data = getBaseService()
                .update(userBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}/_assoc")
    @Operation(summary = "根据主键查询(关联查询)")
    public Mono<TenantUserVO> getAssocById(@PathVariable Long id) {
        return getBaseService()
                .getAssocById(id);
    }
}

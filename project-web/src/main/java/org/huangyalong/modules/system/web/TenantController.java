package org.huangyalong.modules.system.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Tenant;
import org.huangyalong.modules.system.request.TenantBO;
import org.huangyalong.modules.system.request.TenantQueries;
import org.huangyalong.modules.system.service.TenantService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@tenant")
@RestController
@RequestMapping("/tenant")
@Tag(name = "租户管理")
public class TenantController extends ReactorController<
        TenantService,
        Long,
        Tenant,
        TenantQueries,
        TenantBO,
        TenantBO
        > {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(TenantQueries queries) {
        var data = getReactorService().
                getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(TenantBO tenantBO) {
        var data = getReactorService()
                .add(tenantBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(TenantBO tenantBO) {
        var data = getReactorService()
                .update(tenantBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getReactorService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

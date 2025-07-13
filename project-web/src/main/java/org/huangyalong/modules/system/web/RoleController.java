package org.huangyalong.modules.system.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.RoleBO;
import org.huangyalong.modules.system.request.RoleQueries;
import org.huangyalong.modules.system.service.RoleService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.response.OptionVO;
import org.myframework.base.web.ReactorController;
import org.myframework.base.web.extra.OptionController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;

@PreAuth(replace = "@role")
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理")
public class RoleController extends ReactorController<
        RoleService,
        Long,
        Role,
        RoleQueries,
        RoleBO,
        RoleBO
        > implements OptionController<
        Role,
        RoleQueries
        > {

    @Override
    public ApiResponse<QueryWrapper> handlerOption(RoleQueries queries) {
        var query = QueryWrapper.create()
                .select(ROLE.NAME.as(OptionVO::getLabel),
                        ROLE.ID.as(OptionVO::getValue))
                .orderBy(ROLE.ID, Boolean.FALSE);
        var data = getBaseService()
                .getQueryWrapper(queries, query);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(RoleQueries queries) {
        var data = getBaseService().
                getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(RoleBO roleBO) {
        var data = getBaseService()
                .add(roleBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(RoleBO roleBO) {
        var data = getBaseService()
                .update(roleBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

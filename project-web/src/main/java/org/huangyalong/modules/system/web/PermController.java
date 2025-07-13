package org.huangyalong.modules.system.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.PermBO;
import org.huangyalong.modules.system.request.PermQueries;
import org.huangyalong.modules.system.service.PermService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.response.OptionVO;
import org.myframework.base.web.ReactorController;
import org.myframework.base.web.extra.OptionController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;

@PreAuth(replace = "@perm")
@RestController
@RequestMapping("/perm")
@Tag(name = "权限管理")
public class PermController extends ReactorController<
        PermService,
        Long,
        Perm,
        PermQueries,
        PermBO,
        PermBO
        >
        implements OptionController<
        Perm,
        PermQueries
        > {

    @Override
    public ApiResponse<QueryWrapper> handlerOption(PermQueries queries) {
        var query = QueryWrapper.create()
                .select(PERM.NAME.as(OptionVO::getLabel),
                        PERM.ID.as(OptionVO::getValue))
                .orderBy(PERM.ID, Boolean.FALSE);
        var data = getBaseService()
                .getQueryWrapper(queries, query);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(PermQueries queries) {
        var data = getBaseService().
                getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(PermBO permBO) {
        var data = getBaseService()
                .add(permBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(PermBO permBO) {
        var data = getBaseService()
                .update(permBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

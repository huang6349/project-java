package org.huangyalong.modules.system.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.UserBO;
import org.huangyalong.modules.system.request.UserQueries;
import org.huangyalong.modules.system.service.UserService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@user")
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController extends ReactorController<
        UserService,
        Long,
        User,
        UserQueries,
        UserBO,
        UserBO
        > {

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(UserQueries queries) {
        var data = getReactorService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(Long id) {
        var data = getReactorService()
                .getQueryWrapper(id);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(UserBO userBO) {
        var data = getReactorService()
                .add(userBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(UserBO userBO) {
        var data = getReactorService()
                .update(userBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getReactorService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

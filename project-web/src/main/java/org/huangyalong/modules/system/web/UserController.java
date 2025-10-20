package org.huangyalong.modules.system.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.UserBO;
import org.huangyalong.modules.system.request.UserQueries;
import org.huangyalong.modules.system.service.UserService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.base.web.extra.OptionController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.GetMapping;
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
        UserBO>
        implements OptionController<
        User,
        UserQueries> {

    @Override
    public ApiResponse<QueryWrapper> handlerOption(UserQueries queries) {
        var data = getBaseService()
                .getOptionWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(UserQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(UserBO userBO) {
        var data = getBaseService()
                .add(userBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(UserBO userBO) {
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

    @SaCheckLogin
    @GetMapping("/_current")
    @Operation(summary = "获取当前用户信息")
    public Mono<User> current() {
        var id = UserHelper.getLoginIdAsLong();
        return getBaseService()
                .getById(id);
    }
}

package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.UserPasswordBO;
import org.huangyalong.modules.system.service.UserPasswordService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.reactive.ReactiveController;
import org.myframework.base.web.reactive.ReactiveUpdateController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user/password")
@Tag(name = "用户密码")
public class UserPasswordController extends ReactiveController<UserPasswordService, User>
        implements ReactiveUpdateController<User, UserPasswordBO> {

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(UserPasswordBO userPasswordBO) {
        StpUtil.checkLogin();
        var data = getReactorService()
                .update(userPasswordBO);
        return ApiResponse.ok(data);
    }
}

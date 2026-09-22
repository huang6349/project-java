package org.huangyalong.modules.system.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.service.UserTenantService;
import org.myframework.base.response.OptionVO;
import org.myframework.base.web.SuperSimpleController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserTenantController extends SuperSimpleController<UserTenantService, User> {

    @SaCheckLogin
    @GetMapping("/tenant")
    @Operation(summary = "查询可切换租户(选项查询)")
    public Flux<OptionVO> items() {
        return getBaseService()
                .option();
    }
}

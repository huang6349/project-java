package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.Getter;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.response.Authentication;
import org.huangyalong.modules.system.service.UserAuthService;
import org.huangyalong.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Getter
@Service
public class UserAuthServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserAuthService {

    @Autowired
    private UserService userService;

    @Override
    public Mono<Authentication> me() {
        var id = UserHelper.getLoginIdAsLong();
        return getUserService()
                .getById(id)
                .map(Authentication::create);
    }
}

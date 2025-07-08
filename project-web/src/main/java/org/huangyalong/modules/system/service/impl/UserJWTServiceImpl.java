package org.huangyalong.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.crypto.digest.BCrypt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.request.LoginBO;
import org.huangyalong.modules.system.response.JWTToken;
import org.huangyalong.modules.system.service.UserJWTService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AllArgsConstructor
@Service
public class UserJWTServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserJWTService {

    @Override
    public Mono<JWTToken> authorize(LoginBO loginBO) {
        var username = Opt.ofNullable(loginBO)
                .map(LoginBO::getUsername)
                .get();
        var password = Opt.ofNullable(loginBO)
                .map(LoginBO::getPassword)
                .get();
        var query = getBlockService()
                .query()
                .where(USER.USERNAME.eq(username, If::hasText))
                .or(USER.MOBILE.eq(username, If::hasText))
                .or(USER.EMAIL.eq(username, If::hasText));
        var data = getBlockService()
                .getOneOpt(query)
                .orElseThrow(() -> new BusinessException("帐号或密码错误"));
        if (BCrypt.checkpw(password, data.getPassword())) {
            StpUtil.login(data.getId());
        } else throw new BusinessException("帐号或密码错误");
        return Mono.fromSupplier(() -> {
            var idToken = StpUtil.getTokenValue();
            return new JWTToken(idToken);
        });
    }
}

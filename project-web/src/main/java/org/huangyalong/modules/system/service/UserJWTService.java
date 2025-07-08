package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.LoginBO;
import org.huangyalong.modules.system.response.JWTToken;
import reactor.core.publisher.Mono;

public interface UserJWTService extends ReactorService<User> {

    Mono<JWTToken> authorize(LoginBO loginBO);
}

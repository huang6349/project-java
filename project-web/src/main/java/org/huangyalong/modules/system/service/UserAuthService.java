package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.response.Authentication;
import reactor.core.publisher.Mono;

public interface UserAuthService extends ReactorService<User> {

    Mono<Authentication> me();
}

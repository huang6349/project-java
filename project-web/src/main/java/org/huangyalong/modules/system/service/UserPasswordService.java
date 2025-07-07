package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.UserPasswordBO;
import reactor.core.publisher.Mono;

public interface UserPasswordService extends ReactorService<User> {

    Mono<Boolean> update(UserPasswordBO userPasswordBO);
}

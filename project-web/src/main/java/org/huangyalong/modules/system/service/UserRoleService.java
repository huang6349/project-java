package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.UserRoleBO;
import reactor.core.publisher.Mono;

public interface UserRoleService extends ReactorService<Role> {

    Mono<Boolean> assoc(UserRoleBO roleBO);
}

package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.request.UserRoleQueries;
import org.huangyalong.modules.system.response.UserRoleVO;
import reactor.core.publisher.Mono;

public interface UserRoleService extends ReactorService<Role> {

    Mono<UserRoleVO> query(UserRoleQueries queries);

    Mono<Boolean> assoc(UserRoleBO roleBO);
}

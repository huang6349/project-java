package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.UserRoleBO;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;

public interface UserRoleService extends ReactorService<Role> {

    default List<Role> getBlockByUserId(Serializable id) {
        var mono = getByUserId(id);
        return runBlock(mono);
    }

    default List<Role> getBlockByUserId(Object id) {
        var convert = (Serializable) id;
        return getBlockByUserId(convert);
    }

    Mono<List<Role>> getByUserId(Serializable id);

    Mono<Boolean> assoc(UserRoleBO roleBO);
}

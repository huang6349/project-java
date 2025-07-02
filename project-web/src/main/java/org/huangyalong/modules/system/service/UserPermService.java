package org.huangyalong.modules.system.service;

import org.huangyalong.modules.system.domain.Perm;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;

public interface UserPermService {

    default List<Perm> getBlockByUserId(Serializable id) {
        var mono = getAllByUserId(id);
        return runBlock(mono);
    }

    default List<Perm> getBlockByUserId(Object id) {
        var convert = (Serializable) id;
        return getBlockByUserId(convert);
    }

    Mono<List<Perm>> getAllByUserId(Serializable id);
}

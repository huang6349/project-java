package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.RolePermBO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface RolePermService extends ReactorService<Perm> {

    Mono<Boolean> assoc(RolePermBO permBO);

    Flux<Perm> list(Serializable id);
}

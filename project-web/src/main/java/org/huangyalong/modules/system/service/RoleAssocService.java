package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.RoleAssoc;
import org.huangyalong.modules.system.request.RoleAssocBO;
import org.huangyalong.modules.system.request.RoleDissocBO;
import reactor.core.publisher.Mono;

public interface RoleAssocService extends ReactorService<RoleAssoc> {

    Mono<Boolean> dissoc(RoleDissocBO dissocBO);

    Mono<Boolean> assoc(RoleAssocBO assocBO);
}

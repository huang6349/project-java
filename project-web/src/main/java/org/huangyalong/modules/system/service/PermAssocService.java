package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.PermAssoc;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.PermDissocBO;
import reactor.core.publisher.Mono;

public interface PermAssocService extends ReactorService<PermAssoc> {

    Mono<Boolean> dissoc(PermDissocBO dissocBO);

    Mono<Boolean> assoc(PermAssocBO assocBO);
}

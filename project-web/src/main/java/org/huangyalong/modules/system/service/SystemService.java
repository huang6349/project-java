package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.system.domain.System;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;

public interface SystemService extends ReactorService<System> {

    default Mono<Map<String, Object>> configs() {
        return getById(CONFIG_ID)
                .map(System::getConfigs)
                .defaultIfEmpty(Map.of());
    }
}

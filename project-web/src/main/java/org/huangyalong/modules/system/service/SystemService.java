package org.huangyalong.modules.system.service;

import cn.hutool.json.JSONObject;
import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.core.satoken.helper.SystemHelper;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.request.SystemBO;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

public interface SystemService extends ReactorService<System> {

    /**
     * 获取系统配置信息：读 SystemHelper 缓存（按 code 合并为域嵌套 Map，未配置的域不出现），
     * 缓存未命中自动回源；写路径（update、启动同步）负责刷新缓存
     */
    default Mono<Map<String, Object>> configs() {
        return Mono.fromSupplier(SystemHelper::getConfigs)
                .map(JSONObject::getRaw)
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 修改配置信息：按 code upsert，整域覆盖，实现见 SystemServiceImpl
     * <p>
     * yml 同步的域重启后回滚；接口维护的域重启保留
     */
    Mono<Boolean> update(SystemBO systemBO);
}

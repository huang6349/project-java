package org.huangyalong.modules.system.configs;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import lombok.Getter;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.service.SystemService;
import org.myframework.ai.properties.AiProperties;
import org.myframework.core.config.FrameworkAutoTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Mono;

import static cn.hutool.core.lang.Opt.ofNullable;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runAsync;
import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;

@Getter
@Configuration
@AutoConfigureAfter(FrameworkAutoTable.class)
public class SystemConfigsLoader {

    @Autowired
    private TenantProperties tenantProperties;

    @Autowired
    private AiProperties aiProperties;

    @Autowired
    private SystemService systemService;

    /**
     * 应用启动后初始化系统配置到数据库
     */
    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        StaticLog.trace("初始化系统配置");
        runAsync(getSystemService()
                .getById(CONFIG_ID)
                .flatMap(this::initTenantConfigs)
                .flatMap(this::initAiConfigs)
                .flatMap(this::saveOrUpdate));
    }

    /**
     * 初始化租户配置（已存在则跳过）
     */
    Mono<System> initTenantConfigs(System system) {
        if (ObjectUtil.isNotNull(system))
            return Mono.just(system);
        // 获取租户功能是否开启
        var enabled = ofNullable(getTenantProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        // 构建租户配置
        var configs = TenantConfigs.create()
                .addEnabled(enabled)
                .addVersion();
        return Mono.just(System.create()
                .with(configs));
    }

    /**
     * 初始化 AI 配置（每次启动更新）
     */
    Mono<System> initAiConfigs(System system) {
        // 获取智能助手功能是否开启
        var enabled = ofNullable(getAiProperties())
                .map(AiProperties::isEnabled)
                .orElse(Boolean.TRUE);
        // 构建智能助手配置
        var configs = AiConfigs.create()
                .addEnabled(enabled)
                .addVersion();
        return Mono.just(ofNullable(system)
                .orElseGet(System::create)
                .with(configs));
    }

    /**
     * 保存或更新系统配置
     */
    Mono<Boolean> saveOrUpdate(System system) {
        return getSystemService()
                .saveOrUpdate(system);
    }
}

package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.Getter;
import org.huangyalong.modules.system.configs.AiConfigs;
import org.huangyalong.modules.system.configs.TenantConfigs;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.mapper.SystemMapper;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.service.SystemService;
import org.myframework.ai.properties.AiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static cn.hutool.core.lang.Opt.ofNullable;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;

@Getter
@Service
public class SystemServiceImpl extends ReactorServiceImpl<SystemMapper, System> implements SystemService {

    @Autowired
    private TenantProperties tenantProperties;

    @Autowired
    private AiProperties aiProperties;

    /**
     * 初始化系统配置
     * 应用启动后读取配置并初始化到 System 表
     */
    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        StaticLog.trace("初始化系统配置");
        runBlock(getById(CONFIG_ID)
                .flatMap(this::initTenantConfigs)
                .flatMap(this::initAiConfigs)
                .flatMap(this::saveOrUpdate));
    }

    /**
     * 初始化租户配置
     * 如果系统配置已存在则跳过，否则创建新的系统配置
     *
     * @param system 现有的系统配置，可能为 null
     * @return 包含租户配置的系统对象
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
     * 初始化智能助手配置
     * 每次启动都会更新智能助手配置到系统对象中
     *
     * @param system 现有的系统配置
     * @return 合并了智能助手配置的系统对象
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
}

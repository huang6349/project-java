package org.huangyalong.modules.system.configs;

import cn.hutool.core.lang.Opt;
import cn.hutool.log.StaticLog;
import org.huangyalong.core.satoken.helper.SystemHelper;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.request.SystemBO;
import org.myframework.ai.properties.AiProperties;
import org.myframework.core.config.FrameworkAutoTable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import static cn.hutool.extra.spring.SpringUtil.getBean;
import static org.huangyalong.core.constants.SystemConstants.CODE_AI;
import static org.huangyalong.core.constants.SystemConstants.CODE_TENANT;
import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;

@Configuration
@AutoConfigureAfter(FrameworkAutoTable.class)
public class SystemLoader {

    /**
     * 应用启动后初始化系统配置到数据库
     */
    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        StaticLog.trace("初始化系统配置");
        initTenantConfigs();
        initAiConfigs();
        // 同步完成后刷新缓存，保证与库一致
        SystemHelper.load();
    }

    /**
     * 同步租户配置（每次启动整域覆盖）
     */
    Boolean initTenantConfigs() {
        // 获取租户功能是否开启
        var properties = getBean(TenantProperties.class);
        var enabled = Opt.ofNullable(properties)
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        var configs = TenantConfigs.create()
                .addEnabled(enabled)
                .addVersion()
                .getConfigs();
        var systemBO = new SystemBO();
        systemBO.setCode(CODE_TENANT);
        systemBO.setConfigs(configs);
        return sync(systemBO);
    }

    /**
     * 同步智能助手配置（每次启动整域覆盖）
     */
    Boolean initAiConfigs() {
        // 获取智能助手功能是否开启
        var properties = getBean(AiProperties.class);
        var enabled = Opt.ofNullable(properties)
                .map(AiProperties::isEnabled)
                .orElse(Boolean.TRUE);
        var configs = AiConfigs.create()
                .addEnabled(enabled)
                .addVersion()
                .getConfigs();
        var systemBO = new SystemBO();
        systemBO.setCode(CODE_AI);
        systemBO.setConfigs(configs);
        return sync(systemBO);
    }

    /**
     * 按 code 同步配置（存在则覆盖，不存在则新增）
     */
    Boolean sync(SystemBO systemBO) {
        var configs = Opt.ofNullable(systemBO)
                .map(SystemBO::getConfigs)
                .get();
        var code = Opt.ofNullable(systemBO)
                .map(SystemBO::getCode)
                .get();
        return System.create()
                .where(SYSTEM.CODE.eq(code))
                .oneOpt()
                .orElseGet(System::create)
                .setCode(code)
                .setConfigs(configs)
                .saveOrUpdate();
    }
}

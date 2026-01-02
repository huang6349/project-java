package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.Getter;
import org.huangyalong.modules.system.configs.SystemConfigs;
import org.huangyalong.modules.system.configs.TenantConfigs;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.mapper.SystemMapper;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static cn.hutool.core.lang.Opt.ofNullable;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;
import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;

@Getter
@Service
public class SystemServiceImpl extends ReactorServiceImpl<SystemMapper, System> implements SystemService {

    @Autowired
    private TenantProperties tenantProperties;

    /**
     * 初始化系统配置
     * 系统启动后读取 TenantProperties 并初始化到 System 表
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initSystemConfigs() {
        // 检查是否已存在系统配置
        var exists = queryChain()
                .where(SYSTEM.ID.eq(CONFIG_ID))
                .exists();
        if (BooleanUtil.isTrue(exists)) {
            StaticLog.info("系统配置已存在，跳过租户配置初始化");
            return;
        }

        // 获取租户功能是否开启
        var enabled = ofNullable(getTenantProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);

        // 构建租户配置
        var tenantConfigs = TenantConfigs.create()
                .addEnabled(enabled)
                .addVersion()
                .getConfigs();

        // 构建系统配置（包含租户配置和系统版本）
        var configs = SystemConfigs.create()
                .addTenant(tenantConfigs)
                .addVersion()
                .getConfigs();

        // 创建系统配置
        var system = System.create()
                .setConfigs(configs);
        system.setId(CONFIG_ID);
        runBlock(save(system));
    }
}

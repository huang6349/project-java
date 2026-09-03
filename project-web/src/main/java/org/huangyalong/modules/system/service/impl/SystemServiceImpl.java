package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.core.satoken.helper.SystemHelper;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.mapper.SystemMapper;
import org.huangyalong.modules.system.request.SystemBO;
import org.huangyalong.modules.system.service.SystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;

@Service
public class SystemServiceImpl extends ReactorServiceImpl<SystemMapper, System> implements SystemService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(SystemBO systemBO) {
        var code = Opt.ofNullable(systemBO)
                .map(SystemBO::getCode)
                .get();
        var data = System.create()
                .where(SYSTEM.CODE.eq(code))
                .oneOpt()
                .orElseGet(System::create)
                .with(systemBO);
        return saveOrUpdate(data)
                .doOnSuccess(this::refreshCache);
    }

    /**
     * 保存成功后刷新系统配置缓存
     */
    void refreshCache(Boolean saved) {
        if (BooleanUtil.isTrue(saved)) {
            SystemHelper.load();
        }
    }
}

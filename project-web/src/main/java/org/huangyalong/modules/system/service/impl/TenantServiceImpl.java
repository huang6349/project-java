package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Tenant;
import org.huangyalong.modules.system.mapper.TenantMapper;
import org.huangyalong.modules.system.request.TenantBO;
import org.huangyalong.modules.system.service.TenantService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;
import static org.myframework.core.util.ServiceUtil.randomCode;

@AllArgsConstructor
@Service
public class TenantServiceImpl extends ReactorServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(TenantBO tenantBO) {
        var data = Tenant.create()
                .setCode(randomCode())
                .with(tenantBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(TenantBO tenantBO) {
        var id = Opt.ofNullable(tenantBO)
                .map(TenantBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(tenantBO);
        return updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data);
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }
}

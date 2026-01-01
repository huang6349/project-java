package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.Getter;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.request.UserPermQueries;
import org.huangyalong.modules.system.response.UserPermVO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.core.constants.TenantConstants.INVALID;
import static org.huangyalong.core.satoken.helper.PermHelper.fetch;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@Getter
@Service
public class UserPermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements UserPermService {

    @Autowired
    private PermAssocService assocService;

    @Autowired
    private TenantProperties properties;

    @Override
    public Mono<UserPermVO> query(UserPermQueries queries) {
        var tenantId = getTenantId(queries);
        var id = Opt.ofNullable(queries)
                .map(UserPermQueries::getId)
                .orElseThrow(() -> new BusinessException("主键不能为空"));
        var permVO = new UserPermVO();
        permVO.setTenantId(tenantId);
        permVO.setId(id);
        return list(tenantId, id)
                .collectList()
                .map(permVO::with);
    }

    @Override
    public Mono<UserPermVO> all(UserPermQueries queries) {
        var tenantId = getTenantId(queries);
        var id = Opt.ofNullable(queries)
                .map(UserPermQueries::getId)
                .get();
        var permVO = new UserPermVO();
        permVO.setTenantId(tenantId);
        permVO.setId(id);
        permVO.setPermIds(fetch(tenantId, id));
        return Mono.just(permVO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(UserPermBO permBO) {
        var tenantId = Opt.ofNullable(permBO)
                .map(UserPermBO::getTenantId)
                .get();
        var permIds = Opt.ofNullable(permBO)
                .map(UserPermBO::getPermIds)
                .get();
        var id = Opt.ofNullable(permBO)
                .map(UserPermBO::getId)
                .get();
        var assocBO = new PermAssocBO();
        assocBO.setTenantId(tenantId);
        assocBO.setPermIds(permIds);
        assocBO.setAssoc(USER.getTableName());
        assocBO.setId(id);
        return getAssocService()
                .assoc(assocBO);
    }

    Long getTenantId(UserPermQueries queries) {
        var enabled = ofNullable(getProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        if (enabled) {
            return ofNullable(queries)
                    .map(UserPermQueries::getTenantId)
                    .orElseThrow(() -> new BusinessException("租户不能为空"));
        } else return INVALID;
    }
}

package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.Getter;
import org.huangyalong.modules.system.domain.RoleAssoc;
import org.huangyalong.modules.system.domain.RoleAssocsData;
import org.huangyalong.modules.system.mapper.RoleAssocMapper;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.huangyalong.modules.system.request.RoleAssocBO;
import org.huangyalong.modules.system.request.RoleDissocBO;
import org.huangyalong.modules.system.service.RoleAssocService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.core.util.BooleanUtil.isTrue;
import static org.huangyalong.core.constants.TenantConstants.INVALID;
import static org.huangyalong.modules.system.domain.table.RoleAssocTableDef.ROLE_ASSOC;

@Getter
@Service
public class RoleAssocServiceImpl extends ReactorServiceImpl<RoleAssocMapper, RoleAssoc> implements RoleAssocService {

    @Autowired
    private TenantProperties properties;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> dissoc(RoleDissocBO dissocBO) {
        var tenantId = getTenantId(dissocBO);
        var assoc = Opt.ofNullable(dissocBO)
                .map(RoleDissocBO::getAssoc)
                .get();
        var id = Opt.ofNullable(dissocBO)
                .map(RoleDissocBO::getId)
                .get();
        var query = QueryWrapper.create()
                .where(ROLE_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0))
                .and(ROLE_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(ROLE_ASSOC.TENANT_ID.eq(tenantId))
                .and(ROLE_ASSOC.ASSOC.eq(assoc))
                .and(ROLE_ASSOC.ASSOC_ID.eq(id));
        return remove(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(RoleAssocBO assocBO) {
        var tenantId = getTenantId(assocBO);
        var roleIds = Opt.ofNullable(assocBO)
                .map(RoleAssocBO::getRoleIds)
                .get();
        var assoc = Opt.ofNullable(assocBO)
                .map(RoleAssocBO::getAssoc)
                .get();
        var id = Opt.ofNullable(assocBO)
                .map(RoleAssocBO::getId)
                .get();
        var data = RoleAssocsData.create()
                .setAssoc(assoc)
                .setAssocId(id)
                .setTenantId(tenantId)
                .addRoleIds(roleIds)
                .getAssocs();
        return dissoc(assocBO)
                .thenReturn(data)
                .map(getBlockService()::saveBatch);
    }

    Long getTenantId(RoleDissocBO dissocBO) {
        var enabled = ofNullable(getProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        if (isTrue(enabled)) {
            return ofNullable(dissocBO)
                    .map(RoleDissocBO::getTenantId)
                    .get();
        } else return INVALID;
    }

    Long getTenantId(RoleAssocBO assocBO) {
        var enabled = ofNullable(getProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        if (isTrue(enabled)) {
            return ofNullable(assocBO)
                    .map(RoleAssocBO::getTenantId)
                    .get();
        } else return INVALID;
    }
}

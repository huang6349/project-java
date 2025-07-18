package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.RoleAssoc;
import org.huangyalong.modules.system.domain.RoleAssocsData;
import org.huangyalong.modules.system.mapper.RoleAssocMapper;
import org.huangyalong.modules.system.request.RoleAssocBO;
import org.huangyalong.modules.system.request.RoleDissocBO;
import org.huangyalong.modules.system.service.RoleAssocService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.RoleAssocTableDef.ROLE_ASSOC;

@AllArgsConstructor
@Service
public class RoleAssocServiceImpl extends ReactorServiceImpl<RoleAssocMapper, RoleAssoc> implements RoleAssocService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> dissoc(RoleDissocBO dissocBO) {
        var tenantId = Opt.ofNullable(dissocBO)
                .map(RoleDissocBO::getTenantId)
                .get();
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
        // noinspection DuplicatedCode
        var tenantId = Opt.ofNullable(assocBO)
                .map(RoleDissocBO::getTenantId)
                .get();
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
}

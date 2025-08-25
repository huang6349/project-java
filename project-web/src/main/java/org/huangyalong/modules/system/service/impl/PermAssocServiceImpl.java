package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.PermAssoc;
import org.huangyalong.modules.system.domain.PermAssocsData;
import org.huangyalong.modules.system.mapper.PermAssocMapper;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.PermDissocBO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;

@Service
public class PermAssocServiceImpl extends ReactorServiceImpl<PermAssocMapper, PermAssoc> implements PermAssocService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> dissoc(PermDissocBO dissocBO) {
        var tenantId = Opt.ofNullable(dissocBO)
                .map(PermDissocBO::getTenantId)
                .get();
        var assoc = Opt.ofNullable(dissocBO)
                .map(PermDissocBO::getAssoc)
                .get();
        var id = Opt.ofNullable(dissocBO)
                .map(PermDissocBO::getId)
                .get();
        var query = QueryWrapper.create()
                .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0))
                .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                .and(PERM_ASSOC.ASSOC.eq(assoc))
                .and(PERM_ASSOC.ASSOC_ID.eq(id));
        return remove(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(PermAssocBO assocBO) {
        var tenantId = Opt.ofNullable(assocBO)
                .map(PermAssocBO::getTenantId)
                .get();
        var permIds = Opt.ofNullable(assocBO)
                .map(PermAssocBO::getPermIds)
                .get();
        var assoc = Opt.ofNullable(assocBO)
                .map(PermAssocBO::getAssoc)
                .get();
        var id = Opt.ofNullable(assocBO)
                .map(PermAssocBO::getId)
                .get();
        var data = PermAssocsData.create()
                .setAssoc(assoc)
                .setAssocId(id)
                .setTenantId(tenantId)
                .addPermIds(permIds)
                .getAssocs();
        return dissoc(assocBO)
                .thenReturn(data)
                .map(getBlockService()::saveBatch);
    }
}

package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.Getter;
import org.huangyalong.modules.system.domain.PermAssoc;
import org.huangyalong.modules.system.domain.PermAssocsData;
import org.huangyalong.modules.system.mapper.PermAssocMapper;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.PermDissocBO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.core.constants.TenantConstants.INVALID;
import static org.huangyalong.core.satoken.helper.SystemHelper.allowTenant;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;

@Getter
@Service
public class PermAssocServiceImpl extends ReactorServiceImpl<PermAssocMapper, PermAssoc> implements PermAssocService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> dissoc(PermDissocBO dissocBO) {
        var tenantId = getTenantId(dissocBO);
        var assoc = ofNullable(dissocBO)
                .map(PermDissocBO::getAssoc)
                .get();
        var id = ofNullable(dissocBO)
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
        var tenantId = getTenantId(assocBO);
        var permIds = ofNullable(assocBO)
                .map(PermAssocBO::getPermIds)
                .get();
        var assoc = ofNullable(assocBO)
                .map(PermAssocBO::getAssoc)
                .get();
        var id = ofNullable(assocBO)
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

    Long getTenantId(PermDissocBO dissocBO) {
        if (allowTenant()) {
            return ofNullable(dissocBO)
                    .map(PermDissocBO::getTenantId)
                    .orElseThrow(() -> new BusinessException("租户不能为空"));
        } else return INVALID;
    }

    Long getTenantId(PermAssocBO assocBO) {
        if (allowTenant()) {
            return ofNullable(assocBO)
                    .map(PermAssocBO::getTenantId)
                    .orElseThrow(() -> new BusinessException("租户不能为空"));
        } else return INVALID;
    }
}

package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.Getter;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.huangyalong.modules.system.service.UserPermService;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@Getter
@Service
public class UserPermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements UserPermService {

    @Autowired
    private PermAssocService assocService;

    @Autowired
    private UserRoleService roleService;

    @Override
    public Flux<Perm> all(Serializable tenantId,
                          Serializable id) {
        var roles = runBlock(getRoleService()
                .list(tenantId, id)
                .map(Role::getId)
                .collectList());
        var query = QueryWrapper.create()
                .select(PERM.ALL_COLUMNS)
                .from(PERM)
                .leftJoin(PERM_ASSOC)
                .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0));
        if (CollUtil.isNotEmpty(roles))
            query.and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                    .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                    .and(PERM_ASSOC.ASSOC_ID.eq(id))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName())
                            .and(PERM_ASSOC.ASSOC_ID.in(roles))));
        else query.and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                .and(PERM_ASSOC.ASSOC_ID.eq(id)));
        return list(query);
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
}

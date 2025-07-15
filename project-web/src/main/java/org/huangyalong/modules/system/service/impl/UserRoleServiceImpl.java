package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.mapper.RoleMapper;
import org.huangyalong.modules.system.request.RoleAssocBO;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.service.RoleAssocService;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.RoleAssocTableDef.ROLE_ASSOC;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AllArgsConstructor
@Service
public class UserRoleServiceImpl extends ReactorServiceImpl<RoleMapper, Role> implements UserRoleService {

    private final RoleAssocService assocService;

    @Override
    public Mono<List<Role>> getByUserId(Serializable id) {
        var query = QueryWrapper.create()
                .select(ROLE.ALL_COLUMNS)
                .from(ROLE)
                .rightJoin(ROLE_ASSOC)
                .on(ROLE_ASSOC.ROLE_ID.eq(ROLE.ID))
                .where(ROLE_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(ROLE_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(ROLE_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(ROLE_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(ROLE_ASSOC.ASSOC.eq(USER.getTableName()))
                .and(ROLE_ASSOC.ASSOC_ID.eq(id));
        return list(query).collectList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(UserRoleBO roleBO) {
        var tenantId = Opt.ofNullable(roleBO)
                .map(UserRoleBO::getTenantId)
                .get();
        var roleIds = Opt.ofNullable(roleBO)
                .map(UserRoleBO::getRoleIds)
                .get();
        var id = Opt.ofNullable(roleBO)
                .map(UserRoleBO::getId)
                .get();
        var assocBO = new RoleAssocBO();
        assocBO.setTenantId(tenantId);
        assocBO.setRoleIds(roleIds);
        assocBO.setAssoc(USER.getTableName());
        assocBO.setId(id);
        return assocService.assoc(assocBO);
    }
}

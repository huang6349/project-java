package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.service.PermService;
import org.huangyalong.modules.system.service.UserPermService;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AllArgsConstructor
@Service
public class UserPermServiceImpl implements UserPermService {

    private final UserRoleService roleService;

    private final PermService permService;

    @Override
    public Mono<List<Perm>> getAllByUserId(Serializable id) {
        var query = QueryWrapper.create()
                .select(PERM.ALL_COLUMNS)
                .from(PERM)
                .leftJoin(PERM_ASSOC)
                .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0));
        var roles = roleService
                .getBlockByUserId(id)
                .stream()
                .map(Role::getId)
                .toList();
        if (CollUtil.isNotEmpty(roles))
            query.and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                    .and(PERM_ASSOC.ASSOC_ID.eq(id))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName())
                            .and(PERM_ASSOC.ASSOC_ID.in(roles))));
        else query.and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                .and(PERM_ASSOC.ASSOC_ID.eq(id)));
        return permService.list(query)
                .collectList();
    }
}

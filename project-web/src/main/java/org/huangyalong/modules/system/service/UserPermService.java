package org.huangyalong.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.core.satoken.helper.RoleHelper;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.UserPermBO;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface UserPermService extends ReactorService<Perm> {

    default Flux<Perm> list(Serializable tenantId,
                            Serializable id) {
        var query = QueryWrapper.create()
                .select(PERM.ALL_COLUMNS)
                .from(PERM)
                .leftJoin(PERM_ASSOC)
                .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(PERM_ASSOC.ASSOC.eq(USER.getTableName()))
                .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                .and(PERM_ASSOC.ASSOC_ID.eq(id));
        return list(query);
    }

    default Flux<Perm> all(Serializable tenantId,
                           Serializable id) {
        var roles = RoleHelper.load(tenantId, id);
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

    Mono<Boolean> assoc(UserPermBO permBO);
}

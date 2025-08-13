package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.RoleAssocTableDef.ROLE_ASSOC;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface UserRoleService extends ReactorService<Role> {

    default List<Role> getBlockByUserId(Serializable id) {
        var mono = getByUserId(id);
        return runBlock(mono);
    }

    default List<Role> getBlockByUserId(Object id) {
        var convert = (Serializable) id;
        return getBlockByUserId(convert);
    }

    Mono<List<Role>> getByUserId(Serializable id);

    default Flux<Role> list(Serializable tenantId,
                            Serializable id) {
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
                .and(ROLE_ASSOC.TENANT_ID.eq(tenantId))
                .and(ROLE_ASSOC.ASSOC_ID.eq(id));
        return list(query);
    }

    Mono<Boolean> assoc(UserRoleBO roleBO);
}

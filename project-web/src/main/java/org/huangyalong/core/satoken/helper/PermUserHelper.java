package org.huangyalong.core.satoken.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;

import java.io.Serializable;
import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public class PermUserHelper {

    public static List<Long> load(Serializable tenantId,
                                  Serializable id) {
        if (ObjectUtil.isNotEmpty(tenantId) &&
                ObjectUtil.isNotEmpty(id)) {
            var roles = PermRoleHelper.load(id);
            var roleEffective = CollUtil.isNotEmpty(roles);
            return User.create()
                    .select(USER.ID)
                    .leftJoin(PERM_ASSOC)
                    .on(PERM_ASSOC.ASSOC_ID.eq(USER.ID))
                    .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                            .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                    .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                    .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                    .and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                            .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                            .and(PERM_ASSOC.PERM_ID.eq(id)))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName(), roleEffective)
                            .and(PERM_ASSOC.ASSOC_ID.in(roles, roleEffective)))
                    .listAs(Long.class);
        } else return empty();
    }
}

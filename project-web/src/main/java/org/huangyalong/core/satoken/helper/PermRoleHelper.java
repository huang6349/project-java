package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.system.domain.Role;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;

import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;

@UtilityClass
public class PermRoleHelper {

    public static List<Long> fetch(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            return QueryChain.of(Role.class)
                    .select(ROLE.ID)
                    .leftJoin(PERM_ASSOC)
                    .on(PERM_ASSOC.ASSOC_ID.eq(ROLE.ID))
                    .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                            .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                    .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                    .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                    .and(PERM_ASSOC.ASSOC.eq(ROLE.getTableName()))
                    .and(PERM_ASSOC.PERM_ID.eq(id))
                    .listAs(Long.class);
        } else return empty();
    }
}

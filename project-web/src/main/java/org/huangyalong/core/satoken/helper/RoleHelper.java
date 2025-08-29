package org.huangyalong.core.satoken.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.Role;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.redis.RedisHelper;

import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatisflex.core.query.QueryMethods.now;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.core.satoken.helper.UserHelper.getTenant;
import static org.huangyalong.modules.system.domain.table.RoleAssocTableDef.ROLE_ASSOC;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public final class RoleHelper {

    public static List<Long> fetch(Object tenantId,
                                   Object id) {
        if (ObjectUtil.isNotEmpty(tenantId) &&
                ObjectUtil.isNotEmpty(id)) {
            return QueryChain.of(Role.class)
                    .select(ROLE.ID)
                    .leftJoin(ROLE_ASSOC)
                    .on(ROLE_ASSOC.ROLE_ID.eq(ROLE.ID))
                    .where(ROLE_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                            .or(ROLE_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                    .and(ROLE_ASSOC.EFFECTIVE_TIME.ge(now()))))
                    .and(ROLE_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                    .and(ROLE_ASSOC.ASSOC.eq(USER.getTableName()))
                    .and(ROLE_ASSOC.TENANT_ID.eq(tenantId))
                    .and(ROLE_ASSOC.ASSOC_ID.eq(id))
                    .listAs(Long.class);
        } else return empty();
    }

    public static void load(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            var key = format("user:role:{}", id);
            RedisHelper.delete(key);
            var roles = fetch(getTenant(), id)
                    .stream()
                    .map(Convert::toStr)
                    .toList();
            if (ObjectUtil.isNotEmpty(roles))
                RedisHelper.lLeftPushAll(key, roles);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

package org.huangyalong.core.satoken.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import org.huangyalong.modules.system.domain.Perm;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.redis.RedisHelper;

import java.io.Serializable;
import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatisflex.core.query.QueryMethods.now;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public final class PermHelper {

    public static List<Long> load(Serializable tenantId,
                                  Serializable id,
                                  String assoc) {
        if (ObjectUtil.isNotEmpty(tenantId) &&
                ObjectUtil.isNotEmpty(id) &&
                ObjectUtil.isNotEmpty(assoc)) {
            var roles = RoleHelper.load(tenantId, id, assoc);
            var roleEffective = CollUtil.isNotEmpty(roles);
            return Perm.create()
                    .select(PERM.ID)
                    .leftJoin(PERM_ASSOC)
                    .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                    .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                            .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                    .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                    .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                    .and(PERM_ASSOC.ASSOC.eq(assoc)
                            .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                            .and(PERM_ASSOC.ASSOC_ID.eq(id)))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName(), roleEffective)
                            .and(PERM_ASSOC.ASSOC_ID.in(roles, roleEffective)))
                    .listAs(Long.class);
        } else return empty();
    }

    public static List<Long> load(Serializable tenantId,
                                  Serializable id) {
        if (ObjectUtil.isNotEmpty(tenantId) &&
                ObjectUtil.isNotEmpty(id)) {
            var assoc = USER.getTableName();
            return load(tenantId, id, assoc);
        } else return empty();
    }

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user:perm:{}", message);
            RedisHelper.delete(key);
            var id = (Serializable) message;
            var perms = load(UserHelper.getTenant(), id)
                    .stream()
                    .map(Convert::toStr)
                    .toList();
            if (ObjectUtil.isNotEmpty(perms))
                RedisHelper.lLeftPushAll(key, perms);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

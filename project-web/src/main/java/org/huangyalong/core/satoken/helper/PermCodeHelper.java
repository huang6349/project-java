package org.huangyalong.core.satoken.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.system.domain.Perm;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.redis.RedisHelper;

import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static cn.hutool.core.convert.Convert.toLong;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatisflex.core.query.QueryMethods.now;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.core.constants.Constants.SUPER_ADMIN_ID;
import static org.huangyalong.core.satoken.helper.UserHelper.getTenant;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@UtilityClass
public class PermCodeHelper {

    public static List<String> fetch(Object tenantId,
                                     Object id) {
        if (ObjectUtil.isNotEmpty(tenantId) &&
                ObjectUtil.isNotEmpty(id)) {
            var roles = RoleHelper.fetch(tenantId, id);
            var roleEffective = CollUtil.isNotEmpty(roles);
            return QueryChain.of(Perm.class)
                    .select(PERM.CODE)
                    .leftJoin(PERM_ASSOC)
                    .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                    .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                            .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                    .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                    .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                    .and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                            .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                            .and(PERM_ASSOC.ASSOC_ID.eq(id)))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName(), roleEffective)
                            .and(PERM_ASSOC.ASSOC_ID.in(roles, roleEffective)))
                    .listAs(String.class);
        } else return empty();
    }

    public static void load(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            var key = format("user:perm:code:{}", id);
            RedisHelper.delete(key);
            var perms = fetch(getTenant(), id)
                    .stream()
                    .toList();
            if (ObjectUtil.equal(SUPER_ADMIN_ID, toLong(id)))
                RedisHelper.lLeftPushAll(key, "*");
            if (ObjectUtil.isNotEmpty(perms))
                RedisHelper.lLeftPushAll(key, perms);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

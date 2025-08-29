package org.huangyalong.core.satoken.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public final class TenantHelper {

    public static Long fetch(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            return QueryChain.of(User.class)
                    .select(USER.TENANT_ID)
                    .where(USER.ID.eq(id))
                    .oneAs(Long.class);
        } else return null;
    }

    public static void load(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            var key = format("user:tenant:{}", id);
            RedisHelper.delete(key);
            var tenant = Opt.ofNullable(fetch(id))
                    .map(Convert::toStr)
                    .get();
            if (ObjectUtil.isNotEmpty(tenant))
                RedisHelper.set(key, tenant);
            RedisHelper.expire(key, 30, MINUTES);
        }
    }
}

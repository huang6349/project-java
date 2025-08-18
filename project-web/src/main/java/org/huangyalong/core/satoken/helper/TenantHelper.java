package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public final class TenantHelper {

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_tenant_{}", message);
            RedisHelper.delete(key);
            var tenant = User.create()
                    .select(USER.TENANT_ID)
                    .where(USER.ID.eq(message))
                    .oneAs(String.class);
            if (ObjectUtil.isNotEmpty(tenant))
                RedisHelper.set(key, tenant);
            RedisHelper.expire(key, 30, MINUTES);
        }
    }
}

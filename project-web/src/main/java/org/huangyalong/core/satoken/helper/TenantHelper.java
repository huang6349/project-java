package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface TenantHelper {

    static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("account_tenant_{}", message);
            RedisHelper.delete(key);
            var optional = User.create()
                    .select(USER.TENANT_ID)
                    .where(USER.ID.eq(message))
                    .oneAsOpt(String.class);
            if (optional.isPresent()) {
                var tenantId = optional.get();
                RedisHelper.set(key, tenantId);
            }
        }
    }
}

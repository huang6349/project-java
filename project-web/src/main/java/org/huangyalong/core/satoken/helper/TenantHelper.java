package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import lombok.val;
import org.huangyalong.core.satoken.listener.TenantListener;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.convert.Convert.toStr;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@SuppressWarnings("unused")
public interface TenantHelper {

    static void send(Object message) {
        val clazz = TenantListener.class;
        RedisHelper.send(clazz, toStr(message));
    }

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

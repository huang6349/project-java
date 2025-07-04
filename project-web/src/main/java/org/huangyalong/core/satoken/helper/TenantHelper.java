package org.huangyalong.core.satoken.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.service.UserService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;

public final class TenantHelper {

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_tenant_{}", message);
            RedisHelper.delete(key);
            var tenant = SpringUtil.getBean(UserService.class)
                    .getBlockByIdOpt(message)
                    .map(User::getTenantId)
                    .map(Convert::toStr)
                    .orElse(null);
            if (ObjectUtil.isNotNull(tenant)) {
                RedisHelper.set(key, tenant);
                RedisHelper.expire(key, 30, MINUTES);
            }
        }
    }
}

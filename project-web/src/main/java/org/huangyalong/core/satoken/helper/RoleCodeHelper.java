package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;

public final class RoleCodeHelper {

    static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_role_code_{}", message);
            RedisHelper.delete(key);
            var roles = SpringUtil.getBean(UserRoleService.class)
                    .getBlockByUserId(message)
                    .stream()
                    .map(Role::getCode)
                    .toList();
            if (ObjectUtil.isNotEmpty(roles)) {
                RedisHelper.lLeftPushAll(key, roles);
                RedisHelper.expire(key, 1, MINUTES);
            }
        }
    }
}

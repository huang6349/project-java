package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;

public interface RoleHelper {

    static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_role_{}", message);
            RedisHelper.delete(key);
            var roles = SpringUtil.getBean(UserRoleService.class)
                    .getBlockByUserId(message)
                    .stream()
                    .map(Role::getCode)
                    .toList();
            if (ObjectUtil.isNotEmpty(roles)) {
                RedisHelper.lLeftPushAll(key, roles);
            }
        }
    }
}

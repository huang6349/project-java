package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;

public interface PermHelper {

    static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_perm_{}", message);
            RedisHelper.delete(key);
            var perms = SpringUtil.getBean(UserPermService.class)
                    .getBlockByUserId(message)
                    .stream()
                    .map(Perm::getCode)
                    .toList();
            if (ObjectUtil.isNotEmpty(perms)) {
                RedisHelper.lLeftPushAll(key, perms);
            }
        }
    }
}

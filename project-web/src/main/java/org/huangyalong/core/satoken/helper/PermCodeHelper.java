package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.convert.Convert.toLong;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toList;
import static org.huangyalong.core.constants.Constants.SUPER_ADMIN_ID;

public final class PermCodeHelper {

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_perm_code_{}", message);
            RedisHelper.delete(key);
            var perms = SpringUtil.getBean(UserPermService.class)
                    .getBlockByUserId(message)
                    .stream()
                    .map(Perm::getCode)
                    .filter(ObjectUtil::isNotEmpty)
                    .collect(toList());
            if (ObjectUtil.equal(SUPER_ADMIN_ID, toLong(message)))
                RedisHelper.lLeftPushAll(key, "*");
            if (ObjectUtil.isNotEmpty(perms))
                RedisHelper.lLeftPushAll(key, perms);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.service.UserService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;

public final class NicknameHelper {

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_nickname_{}", message);
            RedisHelper.delete(key);
            var optional = SpringUtil.getBean(UserService.class)
                    .getBlockByIdOpt(message);
            var username = optional
                    .map(User::getUsername)
                    .orElse(null);
            var nickname = optional
                    .map(User::getNickname)
                    .orElse(username);
            if (ObjectUtil.isNotNull(nickname))
                RedisHelper.set(key, nickname);
            RedisHelper.expire(key, 30, MINUTES);
        }
    }
}

package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.service.UserService;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;

public interface NicknameHelper {

    static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_nickname_{}", message);
            RedisHelper.delete(key);
            var nickname = SpringUtil.getBean(UserService.class)
                    .getBlockByIdOpt(message)
                    .map(User::getNickname)
                    .orElse(null);
            if (ObjectUtil.isNotNull(nickname)) {
                RedisHelper.set(key, nickname);
            }
        }
    }
}

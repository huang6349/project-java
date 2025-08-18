package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.modules.system.domain.UserExtras.NAME_NICKNAME;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public final class NicknameHelper {

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_nickname_{}", message);
            RedisHelper.delete(key);
            var username = User.create()
                    .select(USER.USERNAME)
                    .where(USER.ID.eq(message))
                    .oneAs(String.class);
            var nickname = User.create()
                    .select(ue(USER.EXTRAS, NAME_NICKNAME).as(User::getNickname))
                    .where(USER.ID.eq(message))
                    .oneAsOpt(String.class)
                    .orElse(username);
            if (ObjectUtil.isNotEmpty(nickname))
                RedisHelper.set(key, nickname);
            RedisHelper.expire(key, 30, MINUTES);
        }
    }
}

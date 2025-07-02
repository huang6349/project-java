package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import lombok.val;
import org.huangyalong.core.satoken.listener.NicknameListener;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.convert.Convert.toStr;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.huangyalong.modules.system.domain.UserExtras.NAME_NICKNAME;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

@SuppressWarnings("unused")
public interface NicknameHelper {

    static void send(Object message) {
        val clazz = NicknameListener.class;
        RedisHelper.send(clazz, toStr(message));
    }

    static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("account_nickname_{}", message);
            RedisHelper.delete(key);
            var optional = User.create()
                    .select(USER.USERNAME)
                    .select(ue(USER.EXTRAS, NAME_NICKNAME).as(User::getNickname))
                    .where(USER.ID.eq(message))
                    .oneOpt();
            if (optional.isPresent()) {
                var username = optional
                        .map(User::getUsername)
                        .orElse(null);
                var nickname = optional
                        .map(User::getNickname)
                        .orElse(username);
                if (ObjectUtil.isNotEmpty(nickname)) {
                    RedisHelper.set(key, username);
                }
            }
        }
    }
}

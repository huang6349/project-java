package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.modules.system.domain.UserExtras.NAME_NICKNAME;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

@UtilityClass
public class NicknameHelper {

    public static String fetch(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            var username = QueryChain.of(User.class)
                    .select(USER.USERNAME)
                    .where(USER.ID.eq(id))
                    .oneAs(String.class);
            return QueryChain.of(User.class)
                    .select(ue(USER.EXTRAS, NAME_NICKNAME).as(User::getNickname))
                    .where(USER.ID.eq(id))
                    .oneAsOpt(String.class)
                    .orElse(username);
        } else return null;
    }

    public static void load(Object id) {
        if (ObjectUtil.isNotEmpty(id)) {
            var key = format("user:nickname:{}", id);
            RedisHelper.delete(key);
            var nickname = fetch(id);
            if (ObjectUtil.isNotEmpty(nickname))
                RedisHelper.set(key, nickname);
            RedisHelper.expire(key, 30, MINUTES);
        }
    }
}

package org.huangyalong.core.satoken.helper;

import cn.hutool.core.lang.Opt;
import lombok.val;
import org.myframework.core.redis.RedisHelper;
import org.myframework.core.satoken.helper.ContextHelper;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.myframework.core.satoken.helper.ContextHelper.getLoginId;

public interface UserHelper extends ContextHelper {

    static String getTenant(Object message) {
        val key = format("account_tenant_{}", message);
        if (!RedisHelper.hasKey(key))
            TenantHelper.load(message);
        return RedisHelper.get(key);
    }

    static String getTenant() {
        return Opt.ofBlankAble(getLoginId())
                .map(UserHelper::getTenant)
                .get();
    }

    static void send(Object message) {
        NicknameHelper.load(message);
        TenantHelper.load(message);
    }
}

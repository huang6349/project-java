package org.huangyalong.core.satoken.helper;

import cn.hutool.core.lang.Opt;
import org.myframework.core.redis.RedisHelper;
import org.myframework.core.satoken.helper.ContextHelper;

import java.util.List;

import static cn.hutool.core.text.CharSequenceUtil.format;

@SuppressWarnings("unused")
public final class UserHelper extends ContextHelper {

    public static List<String> getPermCode(Object message) {
        var key = format("user_perm_code_{}", message);
        if (!RedisHelper.hasKey(key))
            PermCodeHelper.load(message);
        return RedisHelper.lRange(key, 0, -1);
    }

    public static List<String> getPermCode() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getPermCode)
                .get();
    }

    public static List<String> getRoleCode(Object message) {
        var key = format("user_role_code_{}", message);
        if (!RedisHelper.hasKey(key))
            RoleCodeHelper.load(message);
        return RedisHelper.lRange(key, 0, -1);
    }

    public static List<String> getRoleCode() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getRoleCode)
                .get();
    }

    public static String getNickname(Object message) {
        var key = format("user_nickname_{}", message);
        if (!RedisHelper.hasKey(key))
            NicknameHelper.load(message);
        return RedisHelper.get(key);
    }

    public static String getNickname() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getNickname)
                .get();
    }

    public static String getTenant(Object message) {
        var key = format("user_tenant_{}", message);
        if (!RedisHelper.hasKey(key))
            TenantHelper.load(message);
        return RedisHelper.get(key);
    }

    public static String getTenant() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getTenant)
                .get();
    }

    public static void load(Object message) {
        PermCodeHelper.load(message);
        RoleCodeHelper.load(message);
        NicknameHelper.load(message);
        TenantHelper.load(message);
    }
}

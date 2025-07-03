package org.huangyalong.core.satoken.helper;

import cn.hutool.core.lang.Opt;
import org.myframework.core.redis.RedisHelper;
import org.myframework.core.satoken.helper.ContextHelper;

import java.util.List;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.myframework.core.satoken.helper.ContextHelper.getLoginId;

@SuppressWarnings("unused")
public interface UserHelper extends ContextHelper {

    static List<String> getPerms(Object message) {
        var key = format("user_perm_{}", message);
        if (!RedisHelper.hasKey(key))
            PermHelper.load(message);
        return RedisHelper.lRange(key, 0, -1);
    }

    static List<String> getPerms() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getPerms)
                .get();
    }

    static List<String> getRoles(Object message) {
        var key = format("user_role_{}", message);
        if (!RedisHelper.hasKey(key))
            RoleHelper.load(message);
        return RedisHelper.lRange(key, 0, -1);
    }

    static List<String> getRoles() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getRoles)
                .get();
    }

    static String getNickname(Object message) {
        var key = format("user_nickname_{}", message);
        if (!RedisHelper.hasKey(key))
            NicknameHelper.load(message);
        return RedisHelper.get(key);
    }

    static String getNickname() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getNickname)
                .get();
    }

    static String getTenant(Object message) {
        var key = format("user_tenant_{}", message);
        if (!RedisHelper.hasKey(key))
            TenantHelper.load(message);
        return RedisHelper.get(key);
    }

    static String getTenant() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getTenant)
                .get();
    }

    static void load(Object message) {
        NicknameHelper.load(message);
        TenantHelper.load(message);
        PermHelper.load(message);
        RoleHelper.load(message);
    }
}

package org.huangyalong.core.satoken.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import lombok.experimental.UtilityClass;
import org.myframework.core.redis.RedisHelper;
import org.myframework.core.satoken.helper.ContextHelper;

import java.util.List;

import static cn.hutool.core.text.CharSequenceUtil.format;

@UtilityClass
public class UserHelper extends ContextHelper {

    public static List<String> getPermCode(Object message) {
        return Opt.ofNullable(message)
                .map(PermCodeHelper::getPermCode)
                .get();
    }

    public static List<String> getRoleCode(Object message) {
        var key = format("user:role:code:{}", message);
        if (!RedisHelper.hasKey(key))
            RoleCodeHelper.load(message);
        return RedisHelper.lRange(key, 0, -1);
    }

    public static List<String> getRoleCode() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getRoleCode)
                .get();
    }

    public static List<Long> getPerm(Object message) {
        var key = format("user:perm:{}", message);
        if (!RedisHelper.hasKey(key))
            PermHelper.load(message);
        return RedisHelper.lRange(key, 0, -1)
                .stream()
                .map(Convert::toLong)
                .toList();
    }

    public static List<Long> getPerm() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getPerm)
                .get();
    }

    public static List<Long> getRole(Object message) {
        var key = format("user:role:{}", message);
        if (!RedisHelper.hasKey(key))
            RoleHelper.load(message);
        return RedisHelper.lRange(key, 0, -1)
                .stream()
                .map(Convert::toLong)
                .toList();
    }

    public static List<Long> getRole() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getRole)
                .get();
    }

    public static Long getTenant() {
        return Opt.ofNullable(getLoginId())
                .map(TenantHelper::getTenant)
                .get();
    }

    public static String getNickname(Object message) {
        return Opt.ofNullable(message)
                .map(NicknameHelper::getNickname)
                .get();
    }

    public static String getNickname() {
        return Opt.ofNullable(getLoginId())
                .map(UserHelper::getNickname)
                .get();
    }

    public static void load(Object message) {
        PermCodeHelper.load(message);
        RoleCodeHelper.load(message);
        TenantHelper.load(message);
        NicknameHelper.load(message);
    }
}

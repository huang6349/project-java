package org.myframework.core.redis;

import cn.hutool.log.StaticLog;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisHelper extends AbstractRedisHelper {

    /* --------------------key相关操作--------------------- */

    public static void delete(Collection<String> keys) {
        StaticLog.trace("批量删除key: {}", keys);
        getTemplate().delete(keys);
    }

    public static void delete(String key) {
        StaticLog.trace("删除key: {}", key);
        getTemplate().delete(key);
    }

    public static Boolean hasKey(String key) {
        StaticLog.trace("是否存在key: {}", key);
        return getTemplate().hasKey(key);
    }

    public static Boolean expire(String key,
                                 long timeout,
                                 TimeUnit unit) {
        StaticLog.trace("设置key的过期时间: {}", key);
        return getTemplate()
                .expire(key, timeout, unit);
    }

    public static Set<String> keys(String pattern) {
        StaticLog.trace("获取匹配的key: {}", pattern);
        return getTemplate()
                .keys(pattern);
    }

    public static Long getExpire(String key,
                                 TimeUnit unit) {
        StaticLog.trace("获取key的剩余时间: {}", key);
        return getTemplate()
                .getExpire(key, unit);
    }

    public static Long getExpire(String key) {
        StaticLog.trace("获取key的剩余时间: {}", key);
        return getTemplate()
                .getExpire(key);
    }

    /* --------------------string相关操作------------------ */

    public static void setEx(String key,
                             String value,
                             long timeout,
                             TimeUnit unit) {
        StaticLog.trace("设置key的值: {} ，并设置过期时间: {}", key, timeout);
        var ops = getTemplate().opsForValue();
        ops.set(key, value, timeout, unit);
    }

    public static void set(String key, String value) {
        StaticLog.trace("设置key的值: {}", key);
        var ops = getTemplate().opsForValue();
        ops.set(key, value);
    }

    public static String get(String key) {
        StaticLog.trace("获取key的值: {}", key);
        return getTemplate().opsForValue()
                .get(key);
    }

    /* --------------------list相关操作-------------------- */

    public static void lLeftPushAll(String key, Collection<String> value) {
        StaticLog.trace("设置key的值: {}", key);
        var ops = getTemplate().opsForList();
        ops.leftPushAll(key, value);
    }

    public static void lLeftPushAll(String key, String... value) {
        StaticLog.trace("设置key的值: {}", key);
        var ops = getTemplate().opsForList();
        ops.leftPushAll(key, value);
    }

    public static List<String> lRange(String key, long start, long end) {
        StaticLog.trace("获取key的值: {}", key);
        return getTemplate()
                .opsForList()
                .range(key, start, end);
    }
}

package org.myframework.core.redis;

import cn.hutool.log.StaticLog;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.myframework.core.redis.RedisUtil.getChannelName;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RedisHelper extends RedisTemplate {

    private static StringRedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        redisTemplate = stringRedisTemplate;
        StaticLog.trace("RedisHelper 初始化完成，静态模板已注入");
    }

    /* --------------------key相关操作--------------------- */

    public static void delete(Collection<String> keys) {
        StaticLog.trace("批量删除key: {}", keys);
        redisTemplate.delete(keys);
    }

    public static void delete(String key) {
        StaticLog.trace("删除key: {}", key);
        redisTemplate.delete(key);
    }

    public static Boolean hasKey(String key) {
        StaticLog.trace("是否存在key: {}", key);
        return redisTemplate.hasKey(key);
    }

    public static Boolean expire(String key,
                                 long timeout,
                                 TimeUnit unit) {
        StaticLog.trace("设置key的过期时间: {}", key);
        return redisTemplate.expire(key, timeout, unit);
    }

    public static Set<String> keys(String pattern) {
        StaticLog.trace("获取匹配的key: {}", pattern);
        return redisTemplate.keys(pattern);
    }

    public static Long getExpire(String key,
                                 TimeUnit unit) {
        StaticLog.trace("获取key的剩余时间: {}", key);
        return redisTemplate.getExpire(key, unit);
    }

    public static Long getExpire(String key) {
        StaticLog.trace("获取key的剩余时间: {}", key);
        return redisTemplate.getExpire(key);
    }

    /* --------------------string相关操作------------------ */

    public static void setEx(String key,
                             String value,
                             long timeout,
                             TimeUnit unit) {
        StaticLog.trace("设置key的值: {} ，并设置过期时间: {}", key, timeout);
        redisTemplate.opsForValue()
                .set(key, value, timeout, unit);
    }

    public static void set(String key, String value) {
        StaticLog.trace("设置key的值: {}", key);
        redisTemplate.opsForValue()
                .set(key, value);
    }

    public static String get(String key) {
        StaticLog.trace("获取key的值: {}", key);
        return redisTemplate.opsForValue()
                .get(key);
    }

    /* --------------------list相关操作-------------------- */

    public static void lLeftPushAll(String key, Collection<String> value) {
        StaticLog.trace("设置key的值: {}", key);
        redisTemplate.opsForList()
                .leftPushAll(key, value);
    }

    public static void lLeftPushAll(String key, String... value) {
        StaticLog.trace("设置key的值: {}", key);
        redisTemplate.opsForList()
                .leftPushAll(key, value);
    }

    public static List<String> lRange(String key, long start, long end) {
        StaticLog.trace("获取key的值: {}", key);
        return redisTemplate.opsForList()
                .range(key, start, end);
    }

    /* --------------------send相关操作-------------------- */

    public static void send(Class<? extends MessageListener> listener,
                            Object message) {
        var channelName = getChannelName(listener);
        send(channelName, message);
    }

    public static void send(String channelName,
                            Object message) {
        redisTemplate.convertAndSend(channelName, message);
        StaticLog.trace("已发布消息: {}: 到渠道: {}", message, channelName);
    }
}

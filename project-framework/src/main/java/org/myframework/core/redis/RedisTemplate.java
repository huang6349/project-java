package org.myframework.core.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

@SuppressWarnings("LombokGetterMayBeUsed")
public abstract class RedisTemplate {

    @Autowired
    @Getter
    protected StringRedisTemplate stringRedisTemplate;
}

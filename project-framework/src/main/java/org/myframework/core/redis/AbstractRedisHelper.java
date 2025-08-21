package org.myframework.core.redis;

import cn.hutool.log.StaticLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

public abstract class AbstractRedisHelper {

    protected static StringRedisTemplate template;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @PostConstruct
    void init() {

        template = redisTemplate;
        StaticLog.trace("初始化完成，静态模板已注入");
    }
}

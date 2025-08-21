package org.myframework.core.config;

import org.myframework.core.redis.RedisHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisHelper.class)
public class FrameworkRedis {
}

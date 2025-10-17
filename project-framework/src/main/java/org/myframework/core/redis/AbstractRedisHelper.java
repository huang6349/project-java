package org.myframework.core.redis;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import org.springframework.data.redis.core.StringRedisTemplate;

public abstract class AbstractRedisHelper {

    private static volatile StringRedisTemplate template;

    protected static StringRedisTemplate getTemplate() {
        if (template == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractRedisHelper.class) { // 同步锁
                if (template == null) { // 第二次检查，确保只初始化一次
                    try {
                        template = SpringUtil.getBean(StringRedisTemplate.class);
                        StaticLog.trace("初始化完成，静态模板已注入");
                    } catch (Exception e) {
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new RuntimeException("初始化失败", e);
                    }
                }
            }
        }
        return template;
    }
}

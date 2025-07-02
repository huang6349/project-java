package org.myframework.core.redis;

import org.springframework.data.redis.connection.MessageListener;

import static cn.hutool.core.text.CharSequenceUtil.*;
import static cn.hutool.core.util.ClassUtil.getClassName;

public class RedisUtil {

    public static String getChannelName(Class<? extends MessageListener> clazz) {
        var className = getClassName(clazz, Boolean.TRUE);
        var channel = removeSufAndLowerFirst(className, "Subscriber");
        var name = toSymbolCase(channel, '-');
        return format("channel:{}", name);
    }
}

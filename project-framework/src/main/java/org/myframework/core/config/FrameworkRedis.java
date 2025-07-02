package org.myframework.core.config;

import cn.hutool.core.collection.CollUtil;
import org.myframework.core.redis.RedisHelper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static org.myframework.core.redis.RedisUtil.getChannelName;

@Configuration
@Import({RedisHelper.class})
public class FrameworkRedis {

    @Bean("redisMessageListenerContainer")
    RedisMessageListenerContainer container(ObjectProvider<MessageListener> listeners,
                                            RedisConnectionFactory factory) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        if (CollUtil.isNotEmpty(listeners)) {
            for (var listener : listeners) {
                var channelName = getChannelName(listener.getClass());
                var topic = new ChannelTopic(channelName);
                container.addMessageListener(listener, topic);
            }
        }
        return container;
    }
}

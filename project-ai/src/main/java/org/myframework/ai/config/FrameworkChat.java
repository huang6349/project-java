package org.myframework.ai.config;

import org.myframework.ai.properties.ChatProperties;
import org.noear.solon.ai.chat.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrameworkChat {

    @Bean
    ChatModel chatModel(ChatProperties properties) {
        return ChatModel.of(properties.getApiUrl())
                .apiKey(properties.getApiKey())
                .provider(properties.getProvider())
                .model(properties.getModel())
                .build();
    }
}

package org.myframework.ai.core;

import cn.hutool.extra.spring.SpringUtil;
import org.myframework.ai.properties.EmbedProperties;
import org.noear.solon.ai.embedding.EmbeddingModel;

public class AiEmbed {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile EmbeddingModel model;

    public static EmbeddingModel getModel() {
        if (!initialized) {
            synchronized (AiEmbed.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return model;
    }

    public static synchronized void refresh() {
        var properties = SpringUtil.getBean(EmbedProperties.class);
        model = EmbeddingModel.of(properties.getApiUrl())
                .apiKey(properties.getApiKey())
                .provider(properties.getProvider())
                .model(properties.getModel())
                .build();
    }
}

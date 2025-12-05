package org.myframework.ai.core;

import org.myframework.ai.properties.EmbedProperties;
import org.noear.solon.ai.embedding.EmbeddingModel;

import static cn.hutool.extra.spring.SpringUtil.getBean;

public class AiEmbed {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile EmbedProperties embedProperties;

    private static volatile EmbeddingModel model;

    public static EmbeddingModel getModel() {
        if (!initialized) {
            synchronized (AiEmbed.class) {
                if (!initialized) {
                    refresh();
                    embedProperties = getBean(EmbedProperties.class);
                    initialized = Boolean.TRUE;
                }
            }
        }
        return model;
    }

    public static synchronized void refresh() {
        model = EmbeddingModel.of(embedProperties.getApiUrl())
                .apiKey(embedProperties.getApiKey())
                .provider(embedProperties.getProvider())
                .model(embedProperties.getModel())
                .build();
    }
}

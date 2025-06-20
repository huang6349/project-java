package org.myframework.ai;

import cn.hutool.core.util.StrUtil;
import com.agentsflex.core.document.splitter.SimpleDocumentSplitter;
import com.agentsflex.core.store.DocumentStore;
import com.agentsflex.store.redis.RedisVectorStore;
import com.agentsflex.store.redis.RedisVectorStoreConfig;
import lombok.val;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import static cn.hutool.core.net.URLEncodeUtil.encodeAll;
import static cn.hutool.extra.spring.SpringUtil.getBean;

public class AiRag {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile DocumentStore store;

    public static DocumentStore getStore() {
        if (!initialized) {
            synchronized (AiRag.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return store;
    }

    public static synchronized void refresh() {
        val properties = getBean(RedisProperties.class);
        val config = new RedisVectorStoreConfig();
        config.setUri(StrUtil.strBuilder()
                .append("redis://")
                .append(encodeAll(properties.getUsername()))
                .append(":")
                .append(encodeAll(properties.getPassword()))
                .append("@")
                .append(properties.getHost())
                .append(":")
                .append(properties.getPort())
                .append("/")
                .append(properties.getDatabase())
                .toString());
        config.setDefaultCollectionName("rag");
        store = new RedisVectorStore(config);
        store.setDocumentSplitter(new SimpleDocumentSplitter(500));
        store.setEmbeddingModel(AiEmbed.getLlm());
    }
}

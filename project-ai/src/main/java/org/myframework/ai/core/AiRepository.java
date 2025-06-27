package org.myframework.ai.core;

import cn.hutool.extra.spring.SpringUtil;
import io.qdrant.client.QdrantClient;
import org.myframework.ai.properties.QdrantProperties;
import org.noear.solon.ai.rag.RepositoryStorable;
import org.noear.solon.ai.rag.repository.QdrantRepository;

import static cn.hutool.extra.spring.SpringUtil.getApplicationName;
import static io.qdrant.client.QdrantGrpcClient.newBuilder;

public class AiRepository {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile RepositoryStorable repository;

    public static RepositoryStorable getRepository() {
        if (!initialized) {
            synchronized (AiRepository.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return repository;
    }

    public static synchronized void refresh() {
        var properties = SpringUtil.getBean(QdrantProperties.class);
        var host = properties.getHost();
        var port = properties.getPort();
        var client = new QdrantClient(newBuilder(host, port, Boolean.FALSE).build());
        repository = QdrantRepository.builder(AiEmbed.getModel(), client)
                .collectionName(getApplicationName())
                .build();
    }
}

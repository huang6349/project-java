package org.myframework.ai.core;

import io.qdrant.client.QdrantClient;
import org.myframework.ai.properties.QdrantProperties;
import org.noear.solon.ai.rag.RepositoryStorable;
import org.noear.solon.ai.rag.repository.QdrantRepository;

import static cn.hutool.extra.spring.SpringUtil.getActiveProfile;
import static cn.hutool.extra.spring.SpringUtil.getBean;
import static io.qdrant.client.QdrantGrpcClient.newBuilder;

public class AiRepository {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile RepositoryStorable repository;

    private static volatile QdrantProperties qdrantProperties;

    public static RepositoryStorable getRepository() {
        if (!initialized) {
            synchronized (AiRepository.class) {
                if (!initialized) {
                    refresh();
                    qdrantProperties = getBean(QdrantProperties.class);
                    initialized = Boolean.TRUE;
                }
            }
        }
        return repository;
    }

    public static synchronized void refresh() {
        var host = qdrantProperties.getHost();
        var port = qdrantProperties.getPort();
        var client = new QdrantClient(newBuilder(host, port, Boolean.FALSE).build());
        repository = QdrantRepository.builder(AiEmbed.getModel(), client)
                .collectionName(getActiveProfile())
                .build();
    }
}

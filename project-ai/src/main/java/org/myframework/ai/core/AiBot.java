package org.myframework.ai.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.noear.solon.ai.chat.message.ChatMessage;
import org.noear.solon.ai.rag.Document;

import java.io.File;
import java.util.List;

public class AiBot {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile AiBot instance;

    public static AiBot getInstance() {
        if (!initialized) {
            synchronized (AiBot.class) {
                if (!initialized) {
                    instance = new AiBot();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @SneakyThrows
    public List<Document> search(String message) {
        return AiRepository.getRepository()
                .search(message)
                .stream()
                .filter(document -> document.getScore() >= 0.68f)
                .toList();
    }

    @SneakyThrows
    public List<Document> insert(File source) {
        var documents = AiLoader.getInstance()
                .split(source);
        AiRepository.getRepository()
                .insert(documents);
        return documents;
    }

    @SneakyThrows
    public void delete(List<Document> documents) {
        var ids = documents.stream()
                .map(Document::getId)
                .toArray(String[]::new);
        AiRepository.getRepository()
                .delete(ids);
    }

    @SneakyThrows
    public String chat(String message) {
        var messages = CollUtil.<ChatMessage>newArrayList();
        var builder = StrUtil.strBuilder();
        builder.append("###角色设定\n");
        builder.append("你是一个智能知识助手，专注于利用上下文中的信息来提供准确和相关的回答。\n");
        builder.append("###指令\n");
        builder.append("当用户的问题与上下文知识匹配时，利用上下文信息进行回答。如果问题与上下文不匹配，运用自身的推理能力生成合适的回答。\n");
        builder.append("###限制\n");
        builder.append("确保回答清晰简洁，避免提供不必要的细节。始终保持语气友好");
        builder.append("当前时间：");
        builder.append(DateUtil.now());
        var content = builder.toString();
        messages.add(ChatMessage.ofSystem(content));
        messages.add(ChatMessage.ofUserAugment(message, search(message)));
        return AiChat.getModel()
                .prompt(messages)
                .call()
                .getMessage()
                .getContent();
    }
}

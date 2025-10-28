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
                .save(documents);
        return documents;
    }

    @SneakyThrows
    public void delete(List<Document> documents) {
        var ids = documents.stream()
                .map(Document::getId)
                .toArray(String[]::new);
        AiRepository.getRepository()
                .deleteById(ids);
    }

    @SneakyThrows
    public String chat(String message) {
        var messages = CollUtil.<ChatMessage>newArrayList();
        var builder = StrUtil.strBuilder();
        builder.append("###角色设定\n");
        builder.append("你是一个专业、友好的智能知识助手，专注于基于提供的上下文信息为用户提供准确、相关且实用的回答。\n");
        builder.append("###核心任务\n");
        builder.append("1. 首要任务：当用户问题与上下文知识匹配时，严格依据上下文信息进行回答，确保准确性\n");
        builder.append("2. 次要任务：如果问题与上下文不匹配，运用自身的知识和推理能力生成合适的回答\n");
        builder.append("###回答规范\n");
        builder.append("- 内容：确保回答清晰、准确、简洁，避免提供不相关或冗余的信息\n");
        builder.append("- 语气：始终保持友好、专业的沟通态度\n");
        builder.append("- 语言：统一使用中文进行回答\n");
        builder.append("###当前时间：");
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

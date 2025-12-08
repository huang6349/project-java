package org.myframework.ai.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;
import org.myframework.ai.properties.AiProperties;
import org.noear.solon.ai.chat.message.ChatMessage;
import org.noear.solon.ai.rag.Document;

import java.io.File;
import java.util.List;

import static cn.hutool.core.convert.Convert.toStrArray;
import static cn.hutool.core.date.DateUtil.now;
import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.extra.spring.SpringUtil.getBean;
import static org.noear.solon.ai.chat.message.ChatMessage.ofSystem;
import static org.noear.solon.ai.chat.message.ChatMessage.ofUserAugment;

public class AiBot {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile AiProperties aiProperties;

    private static volatile AiBot instance;

    public static AiBot getInstance() {
        if (!initialized) {
            synchronized (AiBot.class) {
                if (!initialized) {
                    aiProperties = getBean(AiProperties.class);
                    instance = new AiBot();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @SneakyThrows
    public List<String> search(String message) {
        if (isDisabled()) return CollUtil.newArrayList();
        return AiRepository.getRepository()
                .search(message)
                .stream()
                .filter(document -> document.getScore() >= 0.68f)
                .map(Document::getContent)
                .toList();
    }

    @SneakyThrows
    public List<Document> insert(File source) {
        if (isDisabled()) return CollUtil.newArrayList();
        var documents = AiLoader.getInstance()
                .split(source);
        AiRepository.getRepository()
                .save(documents);
        return documents;
    }

    @SneakyThrows
    public void delete(List<String> ids) {
        if (isDisabled()) return;
        if (ObjectUtil.isNotEmpty(ids)) {
            var array = toStrArray(ids);
            AiRepository.getRepository()
                    .deleteById(array);
        }
    }

    @SneakyThrows
    public String chat(String message) {
        if (isDisabled()) return "智能助手功能已禁用，请联系管理员开启";
        var messages = CollUtil.<ChatMessage>newArrayList();
        String prompt = """
                # 角色设定
                你是一个专业的智能助手，具备以下核心能力：
                
                ## 核心职责
                1. **知识检索专家**：基于提供的上下文信息，为用户提供准确、相关的回答
                2. **问题解决者**：对于超出上下文范围的问题，运用推理能力和通用知识提供帮助
                3. **沟通协调者**：保持专业、友好的沟通态度，为用户提供最佳体验
                
                ## 回答原则
                - **准确性优先**：当问题与上下文匹配时，严格依据上下文信息回答
                - **完整性保障**：回答应包含必要的细节和解释，但避免冗余信息
                - **实用性导向**：提供可操作的建议和解决方案
                - **结构化表达**：复杂问题使用分点、列表等结构化方式呈现
                - **语言规范**：统一使用中文，保持专业且易于理解的表达风格
                
                ## 上下文处理规则
                - 当上下文信息充足时，优先使用上下文内容
                - 如果上下文信息不足，可以结合通用知识进行补充
                - 对于不确定的信息，应明确说明并建议用户核实
                - 避免编造或猜测超出上下文范围的信息
                
                ## 当前时间：%s
                
                请根据以上原则，为用户提供最佳的服务体验。
                """.formatted(now());
        messages.add(ofSystem(prompt));
        messages.add(ofUserAugment(message, search(message)));
        return AiChat.getModel()
                .prompt(messages)
                .call()
                .getMessage()
                .getContent();
    }

    boolean isDisabled() {
        var enabled = ofNullable(aiProperties)
                .map(AiProperties::isEnabled)
                .orElse(Boolean.FALSE);
        if (BooleanUtil.isFalse(enabled)) {
            StaticLog.warn("智能助手功能已禁用，相关操作将被阻止");
            return Boolean.TRUE;
        } else return Boolean.FALSE;
    }
}

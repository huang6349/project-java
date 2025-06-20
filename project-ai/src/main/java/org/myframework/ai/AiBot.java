package org.myframework.ai;

import cn.hutool.core.util.StrUtil;
import com.agentsflex.core.llm.functions.Function;
import com.agentsflex.core.message.HumanMessage;
import com.agentsflex.core.message.SystemMessage;
import com.agentsflex.core.prompt.HistoriesPrompt;
import lombok.val;

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

    public SystemMessage getSystemMessage() {
        val builder = StrUtil.strBuilder();
        builder.append("你是智能聊天机器人，你叫小井，为智能井盖设备管理提供自动服务。 \n");
        builder.append("警告：使用中文！");
        val content = builder.toString();
        return SystemMessage.of(content);
    }

    public static AiRequest prompt(String content) {
        val prompt = new HistoriesPrompt();
        val systemMessage = getInstance().getSystemMessage();
        prompt.setSystemMessage(systemMessage);
        val message = new HumanMessage(content);
        appendPluginToolFunctions(message);
        appendNativeToolFunctions(message);
        appendKnowledgeFunctions(message);
        prompt.addMessage(message);
        val llm = AiLlm.getLlm();
        return AiRequest.builder()
                .prompt(prompt)
                .llm(llm)
                .build();
    }

    static void appendPluginToolFunctions(HumanMessage message) {
        val functions = AiPluginsHelper.getFunctions();
        for (Function function : functions) {
            message.addFunction(function);
        }
    }

    static void appendNativeToolFunctions(HumanMessage message) {
        val functions = AiFunctionsHelper.getFunctions();
        for (Function function : functions) {
            message.addFunction(function);
        }
    }

    static void appendKnowledgeFunctions(HumanMessage message) {
        val function = AiKnowledgeHelper.getFunction();
        message.addFunction(function);
    }
}

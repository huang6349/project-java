package org.myframework.ai.core;

import cn.hutool.core.collection.CollUtil;
import org.myframework.ai.properties.ChatProperties;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.tool.FunctionTool;

import java.util.List;

import static cn.hutool.extra.spring.SpringUtil.getBean;

public class AiChat {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile ChatProperties chatProperties;

    private static volatile ChatModel model;

    public static ChatModel getModel() {
        if (!initialized) {
            synchronized (AiChat.class) {
                if (!initialized) {
                    refresh();
                    chatProperties = getBean(ChatProperties.class);
                    initialized = Boolean.TRUE;
                }
            }
        }
        return model;
    }

    public static synchronized void refresh() {
        var tools = CollUtil.<FunctionTool>newArrayList();
        appendPluginTools(tools);
        appendNativeTools(tools);
        model = ChatModel.of(chatProperties.getApiUrl())
                .apiKey(chatProperties.getApiKey())
                .provider(chatProperties.getProvider())
                .model(chatProperties.getModel())
                .defaultToolsAdd(tools)
                .build();
    }

    static void appendPluginTools(List<FunctionTool> tools) {
        var aiPlugins = AiPluginsHelper.getTools();
        CollUtil.addAll(tools, aiPlugins);
    }

    static void appendNativeTools(List<FunctionTool> tools) {
        var aiTools = AiToolsHelper.getTools();
        CollUtil.addAll(tools, aiTools);
    }
}

package org.myframework.ai.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.myframework.ai.properties.ChatProperties;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.tool.FunctionTool;

import java.util.List;

public class AiChat {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile ChatModel model;

    public static ChatModel getModel() {
        if (!initialized) {
            synchronized (AiChat.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return model;
    }

    public static synchronized void refresh() {
        var properties = SpringUtil.getBean(ChatProperties.class);
        var tools = CollUtil.<FunctionTool>newArrayList();
        appendPluginTools(tools);
        appendNativeTools(tools);
        model = ChatModel.of(properties.getApiUrl())
                .apiKey(properties.getApiKey())
                .provider(properties.getProvider())
                .model(properties.getModel())
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

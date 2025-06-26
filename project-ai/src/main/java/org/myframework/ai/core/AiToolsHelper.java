package org.myframework.ai.core;

import cn.hutool.extra.spring.SpringUtil;
import org.myframework.ai.annotation.FunctionTools;
import org.noear.solon.ai.chat.tool.FunctionTool;
import org.noear.solon.ai.chat.tool.MethodToolProvider;

import java.util.Collection;
import java.util.List;

public class AiToolsHelper {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile List<FunctionTool> tools;

    public static List<FunctionTool> getTools() {
        if (!initialized) {
            synchronized (AiToolsHelper.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return tools;
    }

    public static synchronized void refresh() {
        tools = SpringUtil.getBeanFactory()
                .getBeansWithAnnotation(FunctionTools.class)
                .values()
                .stream()
                .map(MethodToolProvider::new)
                .map(MethodToolProvider::getTools)
                .flatMap(Collection::stream)
                .toList();
    }
}

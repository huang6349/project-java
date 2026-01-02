package org.myframework.ai.core;

import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;
import org.noear.solon.ai.chat.tool.FunctionTool;

import java.util.List;

@UtilityClass
public class AiPluginsHelper {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile List<FunctionTool> tools;

    public static List<FunctionTool> getTools() {
        if (!initialized) {
            synchronized (AiPluginsHelper.class) {
                if (!initialized) {
                    tools = CollUtil.newArrayList();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return tools;
    }
}

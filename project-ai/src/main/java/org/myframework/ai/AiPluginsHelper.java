package org.myframework.ai;

import cn.hutool.core.collection.ListUtil;
import com.agentsflex.core.llm.functions.Function;

import java.util.List;

public class AiPluginsHelper {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile List<Function> functions;

    public static List<Function> getFunctions() {
        if (!initialized) {
            synchronized (AiPluginsHelper.class) {
                if (!initialized) {
                    functions = ListUtil.empty();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return functions;
    }
}

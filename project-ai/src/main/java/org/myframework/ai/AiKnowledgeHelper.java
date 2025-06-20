package org.myframework.ai;

import com.agentsflex.core.llm.functions.Function;

public class AiKnowledgeHelper {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile Function function;

    public static Function getFunction() {
        if (!initialized) {
            synchronized (AiKnowledgeHelper.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return function;
    }

    public static synchronized void refresh() {
        function = AiKnowledge.builder()
                .name("rag")
                .description("默认知识库")
                .build()
                .getFunction();
    }
}

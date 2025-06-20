package org.myframework.ai;

import com.agentsflex.core.llm.Llm;
import com.agentsflex.llm.qwen.QwenLlm;
import com.agentsflex.llm.qwen.QwenLlmConfig;
import lombok.val;
import org.myframework.ai.properties.LlmProperties;

import static cn.hutool.extra.spring.SpringUtil.getBean;

public class AiLlm {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile Llm llm;

    public static Llm getLlm() {
        if (!initialized) {
            synchronized (AiLlm.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return llm;
    }

    public static synchronized void refresh() {
        val properties = getBean(LlmProperties.class);
        val config = new QwenLlmConfig();
        config.setModel(properties.getModel());
        config.setEndpoint(properties.getEndpoint());
        config.setApiKey(properties.getApiKey());
        config.setApiSecret(properties.getApiSecret());
        llm = new QwenLlm(config);
    }
}

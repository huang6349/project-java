package org.myframework.ai;

import com.agentsflex.core.llm.Llm;
import com.agentsflex.core.llm.response.AiMessageResponse;
import com.agentsflex.core.prompt.Prompt;
import com.agentsflex.core.prompt.ToolPrompt;
import lombok.Builder;
import lombok.Data;
import lombok.val;

@Data
@Builder
public class AiRequest {

    private Prompt prompt;

    private Llm llm;

    public AiMessageResponse call() {
        val res = llm.chat(prompt);
        if (res.isFunctionCall()) {
            val prompt = ToolPrompt.of(res);
            return llm.chat(prompt);
        } else return res;
    }
}

package org.myframework.ai;

import com.agentsflex.core.llm.functions.Function;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiKnowledge {

    private String name;

    private String description;

    public Function getFunction() {
        return new AiKnowledgeFunction()
                .aiKnowledge(this);
    }
}

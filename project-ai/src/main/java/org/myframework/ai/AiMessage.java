package org.myframework.ai;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.agentsflex.core.prompt.TextPrompt;
import com.agentsflex.core.prompt.template.TextPromptTemplate;
import lombok.val;

public class AiMessage {

    static TextPrompt ofUserAugment(String message,
                                    Object context) {
        val templateStr = "{{ message }}\n\n Now: {{ now }}\n\n References: {{ context }}";
        val template = TextPromptTemplate.of(templateStr);
        val rootMap = MapUtil.<String, Object>builder()
                .put("message", message)
                .put("now", DateUtil.now())
                .put("context", context)
                .build();
        return template.format(rootMap);
    }
}

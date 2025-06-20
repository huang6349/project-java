package org.myframework.ai;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.agentsflex.core.document.Document;
import com.agentsflex.core.llm.functions.BaseFunction;
import com.agentsflex.core.llm.functions.Parameter;
import com.agentsflex.core.store.SearchWrapper;
import lombok.val;

import java.util.Map;

import static org.myframework.ai.AiMessage.ofUserAugment;

public class AiKnowledgeFunction extends BaseFunction {

    public AiKnowledgeFunction aiKnowledge(AiKnowledge aiKnowledge) {
        setName(Opt.ofNullable(aiKnowledge)
                .map(AiKnowledge::getName)
                .get());
        setDescription(Opt.ofNullable(aiKnowledge)
                .map(AiKnowledge::getDescription)
                .get());
        return this;
    }

    public AiKnowledgeFunction() {
        val parameter = new Parameter();
        parameter.setName("input");
        parameter.setDescription("要查询的相关知识");
        parameter.setType("string");
        parameter.setRequired(Boolean.TRUE);
        setParameters(new Parameter[]{parameter});
    }

    @Override
    public Object invoke(Map<String, Object> argsMap) {
        val input = MapUtil.getStr(argsMap, "input");
        val wrapper = new SearchWrapper();
        wrapper.setMaxResults(5);
        wrapper.setText(input);
        val builder = StrUtil.strBuilder();
        AiRag.getStore()
                .search(wrapper)
                .stream()
                .filter(document -> document.getScore() >= 0.001)
                .map(Document::getContent)
                .forEach(builder::append);
        val context = builder.toString();
        return ofUserAugment(input, context);
    }
}

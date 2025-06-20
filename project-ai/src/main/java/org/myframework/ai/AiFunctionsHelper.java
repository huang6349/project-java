package org.myframework.ai;

import com.agentsflex.core.llm.functions.Function;
import org.myframework.ai.annotation.AiFunctionsDef;

import java.util.Collection;
import java.util.List;

import static cn.hutool.extra.spring.SpringUtil.getBeanFactory;

public class AiFunctionsHelper {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile List<Function> functions;

    public static List<Function> getFunctions() {
        if (!initialized) {
            synchronized (AiFunctionsHelper.class) {
                if (!initialized) {
                    refresh();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return functions;
    }

    public static synchronized void refresh() {
        functions = getBeanFactory().getBeansWithAnnotation(AiFunctionsDef.class)
                .values()
                .stream()
                .map(AiFunctions::new)
                .map(AiFunctions::getFunctions)
                .flatMap(Collection::stream)
                .toList();
    }
}

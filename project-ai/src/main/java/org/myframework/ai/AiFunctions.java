package org.myframework.ai;

import cn.hutool.core.util.ClassUtil;
import com.agentsflex.core.llm.functions.Function;
import com.agentsflex.core.llm.functions.JavaNativeFunction;
import com.agentsflex.core.llm.functions.annotation.FunctionDef;
import lombok.Getter;
import lombok.val;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AiFunctions {

    private final List<Function> functions = new ArrayList<>();

    public AiFunctions(Object object) {
        val clazz = object.getClass();
        // 添加带注释的工具
        for (Method method : ClassUtil.getPublicMethods(clazz)) {
            if (method.isAnnotationPresent(FunctionDef.class)) {
                val function = new JavaNativeFunction();
                function.setClazz(clazz);
                function.setMethod(method);
                function.setObject(object);
                functions.add(function);
            }
        }
        // 添加自己就是工具的工具
        if (ClassUtil.isAssignable(AiFunctions.class, clazz)) {
            functions.addAll(((AiFunctions) object).getFunctions());
        }
    }
}

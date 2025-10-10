package org.myframework.extra.dict;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(DictRegister.class)
public @interface DictScan {

    /**
     * 要扫描的包路径
     */
    String[] value() default {};
}

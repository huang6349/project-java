package org.myframework.core.satoken.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface PreCheckRole {

    String[] value() default {};

    PreMode mode() default PreMode.AND;

    boolean enabled() default true;
}

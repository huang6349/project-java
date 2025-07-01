package org.myframework.core.satoken.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface PreCheckPermission {

    String[] value() default {};

    PreMode mode() default PreMode.AND;

    String[] orRole() default {};

    boolean enabled() default true;
}

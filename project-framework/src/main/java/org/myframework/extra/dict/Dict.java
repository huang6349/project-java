package org.myframework.extra.dict;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    String category() default "";

    String name();
}

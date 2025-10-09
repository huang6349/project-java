package org.myframework.base.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略响应包装注解
 * 用于标记不需要被{@link ResponseAdvice}包装的控制器方法或类
 * 被标记的方法/类将直接返回原始数据，不进行{@link ApiResponse}包装
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreResponse {
}

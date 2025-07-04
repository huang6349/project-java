package org.huangyalong.config;

import org.myframework.core.exception.ExceptionAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
@RestControllerAdvice
public class ExceptionHandling extends ExceptionAdvice {
}

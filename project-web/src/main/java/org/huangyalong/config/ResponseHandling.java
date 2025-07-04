package org.huangyalong.config;

import org.myframework.base.response.ResponseAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
@RestControllerAdvice
public class ResponseHandling extends ResponseAdvice {
}

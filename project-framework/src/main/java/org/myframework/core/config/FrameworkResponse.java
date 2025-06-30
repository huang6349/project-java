package org.myframework.core.config;

import org.myframework.base.response.ApiResponseAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
@RestControllerAdvice
public class FrameworkResponse extends ApiResponseAdvice {
}

package org.myframework.core.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.myframework.base.response.ApiResponse;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static cn.hutool.core.collection.CollUtil.newHashSet;

@Configuration
@RestControllerAdvice
public class FrameworkResponse implements ResponseBodyAdvice<Object> {

    private final Set<String> excludes = newHashSet("org.springdoc");

    @SuppressWarnings("deprecation")
    public boolean supports(@NotNull MethodParameter methodParameter,
                            @NotNull Class<? extends HttpMessageConverter<?>> aClass) {

        var method = methodParameter.getMethod();

        if (ObjectUtil.isNull(method))
            return Boolean.TRUE;
        var mapping = methodParameter.getMethodAnnotation(RequestMapping.class);
        if (ObjectUtil.isNull(mapping))
            return Boolean.FALSE;
        for (var produce : mapping.produces()) {
            var mimeType = MimeType.valueOf(produce);
            if (MediaType.APPLICATION_STREAM_JSON.includes(mimeType))
                return Boolean.FALSE;
            if (MediaType.TEXT_EVENT_STREAM.includes(mimeType)) {
                return Boolean.FALSE;
            }
        }

        if (CollUtil.isNotEmpty(excludes)) {
            var typeName = method.getDeclaringClass().getName() + "." + method.getName();
            for (var exclude : excludes) {
                if (typeName.startsWith(exclude)) {
                    return Boolean.FALSE;
                }
            }
        }

        var returnType = method.getReturnType();
        if (Publisher.class.isAssignableFrom(returnType)) {
            var type = ResolvableType.forMethodParameter(methodParameter);
            returnType = type.resolveGeneric(0);
        }

        var isAlreadyResponse = returnType == ResponseEntity.class ||
                returnType == ApiResponse.class;

        return BooleanUtil.negate(isAlreadyResponse);
    }

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {

        if (body instanceof Mono)
            return ((Mono<?>) body)
                    .map(ApiResponse::ok)
                    .switchIfEmpty(Mono.fromSupplier(ApiResponse::ok));

        if (body instanceof Flux)
            return ((Flux<?>) body)
                    .collectList()
                    .map(ApiResponse::ok)
                    .switchIfEmpty(Mono.fromSupplier(ApiResponse::ok));

        if (body instanceof String)
            return JSONUtil.toJsonStr(ApiResponse.ok(body));

        return ApiResponse.ok(body);
    }
}

package org.myframework.base.response;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static cn.hutool.core.collection.CollUtil.newHashSet;
import static cn.hutool.core.text.CharSequenceUtil.trim;
import static cn.hutool.core.util.BooleanUtil.negate;
import static cn.hutool.json.JSONUtil.toJsonStr;
import static reactor.core.publisher.Mono.fromSupplier;

/**
 * 全局响应处理增强器
 * 用于统一包装API接口的返回值为{@link ApiResponse}格式
 * 支持通过注解、包路径排除等方式忽略特定接口的包装处理
 */
@SuppressWarnings({"deprecation", "ReactiveStreamsUnusedPublisher"})
public abstract class ResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 需要排除的包路径集合，
     * 这些路径下的接口返回值不会被包装
     */
    protected final Set<String> excludes = newHashSet("org.springdoc");

    /**
     * 判断当前响应是否需要被增强(包装)
     *
     * @param methodParameter 方法参数对象，包含当前处理的方法信息
     * @param aClass          消息转换器类型
     * @return 是否需要增强：true-需要包装，false-不需要包装
     */
    @Override
    public boolean supports(@NotNull MethodParameter methodParameter,
                            @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        // 获取当前处理的方法对象
        var method = methodParameter.getMethod();

        // 如果方法对象为 null，默认需要增强
        if (ObjectUtil.isNull(method))
            return Boolean.TRUE;

        // 检查方法是否有 @IgnoreResponse 注解，如果有则不进行包装
        if (method.isAnnotationPresent(IgnoreResponse.class))
            return Boolean.FALSE;

        // 检查方法所在的类是否有 @IgnoreResponse 注解，如果有则不进行包装
        if (method.getDeclaringClass().isAnnotationPresent(IgnoreResponse.class))
            return Boolean.FALSE;

        // 检查方法是否有 @RequestMapping 注解(包括其派生注解如 @GetMapping)
        var mapping = methodParameter.getMethodAnnotation(RequestMapping.class);
        if (ObjectUtil.isNull(mapping))
            return Boolean.FALSE;

        // 检查响应类型是否为流式响应，如果是则不包装
        for (var produce : mapping.produces()) {
            var mimeType = MimeType.valueOf(produce);
            // 排除 application/stream+json 类型响应
            if (MediaType.APPLICATION_STREAM_JSON.includes(mimeType))
                return Boolean.FALSE;
            // 排除 text/event-stream 类型响应(SSE)
            if (MediaType.TEXT_EVENT_STREAM.includes(mimeType)) {
                return Boolean.FALSE;
            }
        }

        // 检查当前方法是否在排除的包路径下，如果是则不包装
        if (CollUtil.isNotEmpty(excludes)) {
            var className = method.getDeclaringClass().getName();
            for (var exclude : excludes) {
                if (className.startsWith(exclude)) {
                    return Boolean.FALSE;
                }
            }
        }

        // 处理响应式类型，获取其泛型实际类型
        var returnType = method.getReturnType();
        if (Publisher.class.isAssignableFrom(returnType)) {
            try {
                var type = ResolvableType.forMethodParameter(methodParameter);
                // 安全处理泛型解析
                if (type.hasGenerics()) {
                    var genericType = type.resolveGeneric(0);
                    if (ObjectUtil.isNotNull(genericType)) {
                        returnType = genericType;
                    }
                }
            } catch (Exception e) {
                // 泛型解析失败时，保持原有返回类型
            }
        }

        // 判断返回类型是否已经是包装类型，如果是则不再包装
        var isAlreadyResponse = returnType == ResponseEntity.class ||
                returnType == ApiResponse.class;

        // 返回是否需要包装的结果
        return negate(isAlreadyResponse);
    }

    /**
     * 对响应体进行包装处理
     * 在响应返回给客户端之前被调用
     *
     * @param body                  原始响应体
     * @param returnType            方法返回类型
     * @param selectedContentType   选中的内容类型
     * @param selectedConverterType 选中的消息转换器类型
     * @param request               服务器请求对象
     * @param response              服务器响应对象
     * @return 包装后的响应体
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {
        // 处理 Mono 类型响应
        if (body instanceof Mono<?> mono) {
            return mono.map(ApiResponse::ok)
                    .switchIfEmpty(fromSupplier(ApiResponse::ok));
        }

        // 处理 Flux 类型响应，先收集为列表再包装
        if (body instanceof Flux<?> flux) {
            return flux.collectList()
                    .map(ApiResponse::ok)
                    .switchIfEmpty(fromSupplier(ApiResponse::ok));
        }

        // 处理 String 类型响应，需要手动序列化为JSON
        if (body instanceof String str) {
            // 处理null、空字符串或仅包含空白字符的情况
            var strBody = ObjectUtil.isEmpty(trim(str)) ? "" : str;
            return toJsonStr(ApiResponse.ok(strBody));
        }

        // 其他类型直接包装为ApiResponse
        return ApiResponse.ok(body);
    }
}

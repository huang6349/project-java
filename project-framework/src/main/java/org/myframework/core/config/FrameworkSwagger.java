package org.myframework.core.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.response.ResponseAdvice;
import org.myframework.core.swagger.SwaggerProperties;
import org.reactivestreams.Publisher;
import org.springdoc.core.ReturnTypeParser;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.webmvc.core.SpringDocWebMvcConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Configuration
@AutoConfigureBefore(SpringDocWebMvcConfiguration.class)
public class FrameworkSwagger {

    @Bean
    GlobalOpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            var paths = openApi.getPaths();
            if (ObjectUtil.isNotNull(paths)) {
                paths.forEach((name, item) -> {
                    var operations = item.readOperations();
                    operations.forEach(operation -> {
                        var securityItem = new SecurityRequirement()
                                .addList("Token");
                        operation.addSecurityItem(securityItem);
                    });
                });
            }
        };
    }

    @Bean
    ReturnTypeParser operationCustomizer(ResponseAdvice advice) {
        // 自定义文档的返回类型，使用ResponseAdvice判断是否需要包装
        return new ReturnTypeParser() {
            @Override
            public Type getReturnType(MethodParameter methodParameter) {
                var shouldWrap = advice.supports(methodParameter, MappingJackson2HttpMessageConverter.class);
                var type = ReturnTypeParser.super.getReturnType(methodParameter);
                if (!shouldWrap)
                    return type;

                var rawType = type instanceof ParameterizedType
                        ? ((ParameterizedType) type).getRawType()
                        : type;

                if (rawType instanceof Class<?> rawClass) {
                    if (rawClass == ResponseEntity.class)
                        return type;
                    if (rawClass == ApiResponse.class)
                        return type;
                    if (rawClass == Void.class)
                        return type;
                    if (rawClass == Void.TYPE)
                        return type;

                    var returnPublisher = Publisher.class.isAssignableFrom(rawClass);
                    var actualType = returnPublisher
                            ? ((type instanceof ParameterizedType)
                            ? ((ParameterizedType) type).getActualTypeArguments()[0]
                            : type)
                            : type;
                    var resolvableType = ResolvableType.forType(actualType);
                    var returnList = Flux.class.isAssignableFrom(rawClass);

                    if (returnPublisher) {
                        return ResolvableType.forClassWithGenerics(
                                Mono.class,
                                ResolvableType.forClassWithGenerics(
                                        ApiResponse.class,
                                        returnList ? ResolvableType.forClassWithGenerics(
                                                List.class,
                                                resolvableType
                                        ) : resolvableType
                                )
                        ).getType();
                    } else {
                        return ResolvableType.forClassWithGenerics(
                                ApiResponse.class,
                                returnList ? ResolvableType.forClassWithGenerics(
                                        List.class,
                                        resolvableType
                                ) : resolvableType
                        ).getType();
                    }
                }
                return type;
            }
        };
    }

    @Bean
    OpenAPI openAPI(SwaggerProperties properties) {
        var securitySchemesItem = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .description("认证Token")
                .name(StpUtil.getTokenName())
                .in(SecurityScheme.In.HEADER);
        var components = new Components()
                .addSecuritySchemes("Token", securitySchemesItem);
        var contact = new Contact()
                .name(properties.getContactName());
        var info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .contact(contact)
                .version(properties.getVersion());
        var securityItem = new SecurityRequirement()
                .addList("Token");
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityItem)
                .components(components);
    }
}

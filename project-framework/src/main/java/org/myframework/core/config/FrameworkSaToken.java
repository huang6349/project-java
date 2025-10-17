package org.myframework.core.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import org.myframework.core.satoken.interceptor.SaTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static cn.hutool.extra.spring.SpringUtil.getApplicationName;

@Configuration
public class FrameworkSaToken implements WebMvcConfigurer {

    private final SaFilterAuthStrategy beforeAuth = obj -> {
        // saToken跨域配置
        SaHolder.getResponse()
                .setHeader("Access-Control-Allow-Origin", "*")
                .setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH")
                .setHeader("Access-Control-Max-Age", "3600")
                .setHeader("Access-Control-Allow-Headers", "*")
                // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                .setHeader("X-XSS-Protection", "1; mode=block");
        // 如果是预检请求，则立即返回到前端
        SaRouter.match(SaHttpMethod.OPTIONS).back();
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowCredentials(Boolean.TRUE)
                .maxAge(3600L);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaTokenInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    SaServletFilter saServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setBeforeAuth(beforeAuth);
    }

    @Bean
    @Primary
    SaTokenConfig saTokenConfig() {
        var config = new SaTokenConfig();
        config.setIsWriteHeader(Boolean.TRUE);
        config.setIsConcurrent(Boolean.TRUE);
        config.setIsShare(Boolean.TRUE);
        config.setIsPrint(Boolean.FALSE);
        config.setJwtSecretKey(getApplicationName());
        return config;
    }
}

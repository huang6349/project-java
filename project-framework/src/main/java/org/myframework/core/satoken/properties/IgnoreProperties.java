package org.myframework.core.satoken.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.collection.CollUtil.newArrayList;

@Data
@Configuration
@ConfigurationProperties("app.security.ignore")
@ToString(callSuper = true)
public class IgnoreProperties {

    // 基础忽略鉴权地址
    private List<String> baseUrl = newArrayList(
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/**/*.ico",
            "/**/*.jpg",
            "/**/*.png",
            "/**/*.gif",
            "/**/api-docs/**",
            "/**/api-docs-ext/**",
            "/**/swagger-resources/**",
            "/**/webjars/**",
            "/druid/**",
            "/error",
            // websocket相关
            "/ws/**"
    );

    // 忽略鉴权的地址
    private List<String> ignoreUrl = newArrayList();

    public List<String> getNotMatchUrl() {
        return new ArrayList<>() {{
            addAll(baseUrl);
            addAll(ignoreUrl);
        }};
    }
}

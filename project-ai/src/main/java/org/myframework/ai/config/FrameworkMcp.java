package org.myframework.ai.config;

import cn.hutool.log.StaticLog;
import org.myframework.ai.mcp.IMcpServerEndpoint;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.web.servlet.SolonServletFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Configuration
public class FrameworkMcp {

    @Bean("startFrameworkMcp")
    FrameworkMcp frameworkMcp(ObjectProvider<IMcpServerEndpoint> serverEndpoints) {
        StaticLog.trace("启动智能助手");
        Solon.start(FrameworkMcp.class, new String[]{"--cfg=mcpserver.yml"});
        serverEndpoints.forEach(serverEndpoint -> {
            var anno = findAnnotation(serverEndpoint.getClass(), McpServerEndpoint.class);
            if (anno != null) {
                var serverEndpointProvider = McpServerEndpointProvider.builder()
                        .from(serverEndpoint.getClass(), anno)
                        .build();
                serverEndpointProvider.addTool(new MethodToolProvider(serverEndpoint));
                serverEndpointProvider.addResource(new MethodResourceProvider(serverEndpoint));
                serverEndpointProvider.addPrompt(new MethodPromptProvider(serverEndpoint));
                serverEndpointProvider.postStart();
            }
        });
        return this;
    }

    @Bean
    FilterRegistrationBean<SolonServletFilter> mcpServerFilter() {
        var filter = new FilterRegistrationBean<SolonServletFilter>();
        filter.setName("SolonFilter");
        filter.addUrlPatterns("/mcp/*");
        filter.setFilter(new SolonServletFilter());
        return filter;
    }

    @PreDestroy
    void destroy() {
        StaticLog.trace("停止智能助手");
        if (Solon.app() != null) {
            var delay = Solon.cfg().stopDelay();
            Solon.stopBlock(Boolean.FALSE, delay);
        }
    }
}

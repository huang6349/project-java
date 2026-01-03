package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.configs.AiConfigs;
import org.huangyalong.modules.system.configs.SystemConfigs;
import org.huangyalong.modules.system.configs.TenantConfigs;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static cn.hutool.core.convert.Convert.toBool;
import static cn.hutool.extra.spring.SpringUtil.getProperty;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@IntegrationTest
class SystemControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void configs() {
        testClient.get()
                .uri("/system/configs")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.tenant.enabled")
                .value(is(toBool(getProperty("app.tenant.enabled"))))
                .jsonPath("$.data.tenant.version")
                .value(is(TenantConfigs.VERSION))
                .jsonPath("$.data.ai.enabled")
                .value(is(toBool(getProperty("app.ai.enabled"))))
                .jsonPath("$.data.ai.version")
                .value(is(AiConfigs.VERSION))
                .jsonPath("$.data.version")
                .value(is(SystemConfigs.VERSION));
    }
}

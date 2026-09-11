package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.request.SystemUtil;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.is;
import static org.huangyalong.core.constants.SystemConstants.*;

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
                .jsonPath("$.data.tenant.readonly")
                .value(is(SystemUtil.isReadonly(CODE_TENANT)))
                .jsonPath("$.data.iot.readonly")
                .value(is(SystemUtil.isReadonly(CODE_IOT)))
                .jsonPath("$.data.ai.readonly")
                .value(is(SystemUtil.isReadonly(CODE_AI)));
    }
}

package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@IntegrationTest
class DictControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void items() {
        testClient.get()
                .uri("/dict/{category:.+}/items", "data-source")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[*].label")
                .value(hasItem("批量上传"))
                .jsonPath("$.data.[*].value")
                .value(hasItem("1"));
    }

    @Order(2)
    @Test
    void query() {
        testClient.get()
                .uri("/dict/{category:.+}", "data-source")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.name")
                .value(is("数据来源"));
    }
}

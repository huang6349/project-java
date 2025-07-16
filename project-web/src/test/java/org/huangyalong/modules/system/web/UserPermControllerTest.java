package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.PermAssoc;
import org.huangyalong.modules.system.enums.PermStatus;
import org.huangyalong.modules.system.request.PermUtil;
import org.huangyalong.modules.system.request.UserPermUtil;
import org.huangyalong.modules.system.request.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@IntegrationTest
class UserPermControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        testClient.post()
                .uri("/perm")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PermUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.post()
                .uri("/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
    }

    @Order(1)
    @Test
    void assoc() {
        var beforeSize = PermAssoc.create()
                .count();
        testClient.patch()
                .uri("/user/perm")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserPermUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/user/perm/{id:.+}", UserUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].name")
                .value(is(PermUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].code")
                .value(is(PermUtil.DEFAULT_CODE))
                .jsonPath("$.data.[0].desc")
                .value(is(PermUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(PermStatus.TYPE0.getValue()));
        var afterSize = PermAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }
}

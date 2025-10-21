package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.enums.UserStatus;
import org.huangyalong.modules.system.request.UserUtil;
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
class UserAuthControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void me() {
        var beforeSize = User.create()
                .count();
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
        StpUtil.login(UserUtil.getId());
        testClient.get()
                .uri("/user/me")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.user.username")
                .value(is(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.user.nickname")
                .value(is(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.user.gender")
                .value(is(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.user.mobile")
                .value(is(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.user.email")
                .value(is(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.user.address")
                .value(is(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.user.desc")
                .value(is(UserUtil.DEFAULT_DESC))
                .jsonPath("$.data.user.status")
                .value(is(UserStatus.TYPE0.getValue()));
        StpUtil.login(DEFAULT_LOGIN);
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }
}

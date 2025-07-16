package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.domain.UserExtras;
import org.huangyalong.modules.system.enums.UserGender;
import org.huangyalong.modules.system.enums.UserStatus;
import org.huangyalong.modules.system.request.PasswordUtil;
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
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AutoConfigureMockMvc
@IntegrationTest
class UserPasswordControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void update() {
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
        var user = User.create()
                .orderBy(USER.ID, Boolean.FALSE)
                .one();
        assertThat(user)
                .isNotNull();
        assertThat(user.getId())
                .isNotNull();
        StpUtil.login(user.getId());
        testClient.put()
                .uri("/user/password")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PasswordUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        StpUtil.login(DEFAULT_LOGIN);
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = UserUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getUsername())
                .isEqualTo(UserUtil.DEFAULT_USERNAME);
        assertThat(BCrypt.checkpw(UserUtil.UPDATED_PASSWORD, testEntity.getPassword()))
                .isTrue();
        assertThat(testEntity.getSalt())
                .isNotNull();
        assertThat(testEntity.getMobile())
                .isEqualTo(UserUtil.DEFAULT_MOBILE);
        assertThat(testEntity.getEmail())
                .isEqualTo(UserUtil.DEFAULT_EMAIL);
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .hasSize(4);
        assertThat(testEntity.getDesc())
                .isEqualTo(UserUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(UserStatus.TYPE0);
        assertThat(testEntity.getTenantId())
                .isNull();
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(4);
        assertThat(testExtras.getByPath(UserExtras.NAME_NICKNAME))
                .isEqualTo(UserUtil.DEFAULT_NICKNAME);
        assertThat(testExtras.getByPath(UserExtras.NAME_GENDER))
                .isEqualTo(UserGender.TYPE0.getValue());
        assertThat(testExtras.getByPath(UserExtras.NAME_ADDRESS))
                .isEqualTo(UserUtil.DEFAULT_ADDRESS);
    }
}

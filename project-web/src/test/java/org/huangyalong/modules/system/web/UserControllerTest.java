package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.domain.UserExtras;
import org.huangyalong.modules.system.enums.UserGender;
import org.huangyalong.modules.system.enums.UserStatus;
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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AutoConfigureMockMvc
@IntegrationTest
class UserControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        var id = 10000000000000000L;
        StpUtil.login(id);
    }

    @Order(1)
    @Test
    void add() {
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
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testUser = User.create()
                .orderBy(USER.ID, Boolean.FALSE)
                .one();
        assertThat(testUser)
                .isNotNull();
        assertThat(testUser.getId())
                .isNotNull();
        assertThat(testUser.getUsername())
                .isEqualTo(UserUtil.DEFAULT_USERNAME);
        assertThat(BCrypt.checkpw(UserUtil.DEFAULT_PASSWORD, testUser.getPassword()))
                .isTrue();
        assertThat(testUser.getSalt())
                .isNotNull();
        assertThat(testUser.getMobile())
                .isEqualTo(UserUtil.DEFAULT_MOBILE);
        assertThat(testUser.getEmail())
                .isEqualTo(UserUtil.DEFAULT_EMAIL);
        assertThat(testUser.getConfigs())
                .isNull();
        assertThat(testUser.getExtras())
                .hasSize(4);
        assertThat(testUser.getDesc())
                .isEqualTo(UserUtil.DEFAULT_DESC);
        assertThat(testUser.getStatus())
                .isEqualTo(UserStatus.TYPE0);
        assertThat(testUser.getTenantId())
                .isNull();
        var extras = testUser.getExtras();
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

    @Order(2)
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
        testClient.put()
                .uri("/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserUtil.createBO(user.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testUser = User.create()
                .orderBy(USER.ID, Boolean.FALSE)
                .one();
        assertThat(testUser)
                .isNotNull();
        assertThat(testUser.getId())
                .isNotNull();
        assertThat(testUser.getUsername())
                .isEqualTo(UserUtil.DEFAULT_USERNAME);
        assertThat(BCrypt.checkpw(UserUtil.UPDATED_PASSWORD, testUser.getPassword()))
                .isTrue();
        assertThat(testUser.getSalt())
                .isNotNull();
        assertThat(testUser.getMobile())
                .isEqualTo(UserUtil.UPDATED_MOBILE);
        assertThat(testUser.getEmail())
                .isEqualTo(UserUtil.UPDATED_EMAIL);
        assertThat(testUser.getConfigs())
                .isNull();
        assertThat(testUser.getExtras())
                .hasSize(4);
        assertThat(testUser.getDesc())
                .isEqualTo(UserUtil.UPDATED_DESC);
        assertThat(testUser.getStatus())
                .isEqualTo(UserStatus.TYPE0);
        assertThat(testUser.getTenantId())
                .isNull();
        var extras = testUser.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(4);
        assertThat(testExtras.getByPath(UserExtras.NAME_NICKNAME))
                .isEqualTo(UserUtil.UPDATED_NICKNAME);
        assertThat(testExtras.getByPath(UserExtras.NAME_GENDER))
                .isEqualTo(UserGender.TYPE1.getValue());
        assertThat(testExtras.getByPath(UserExtras.NAME_ADDRESS))
                .isEqualTo(UserUtil.UPDATED_ADDRESS);
    }

    @Order(3)
    @Test
    void queryPage() {
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
        testClient.get()
                .uri("/user/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[*].username")
                .value(hasItem(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.list.[*].nickname")
                .value(hasItem(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.list.[*].gender")
                .value(hasItem(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.list.[*].mobile")
                .value(hasItem(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.list.[*].email")
                .value(hasItem(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.list.[*].address")
                .value(hasItem(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.list.[*].desc")
                .value(hasItem(UserUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[*].status")
                .value(hasItem(UserStatus.TYPE0.getValue()));
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
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
        testClient.get()
                .uri("/user/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[*].username")
                .value(hasItem(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.[*].nickname")
                .value(hasItem(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.[*].gender")
                .value(hasItem(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.[*].mobile")
                .value(hasItem(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.[*].email")
                .value(hasItem(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.[*].address")
                .value(hasItem(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.[*].desc")
                .value(hasItem(UserUtil.DEFAULT_DESC))
                .jsonPath("$.data.[*].status")
                .value(hasItem(UserStatus.TYPE0.getValue()));
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
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
        testClient.get()
                .uri("/user/{id:.+}", user.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.username")
                .value(is(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.nickname")
                .value(is(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.gender")
                .value(is(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.mobile")
                .value(is(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.email")
                .value(is(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.address")
                .value(is(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.desc")
                .value(is(UserUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(UserStatus.TYPE0.getValue()));
        var afterSize = User.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
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
        testClient.delete()
                .uri("/user/{id:.+}", user.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = User.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

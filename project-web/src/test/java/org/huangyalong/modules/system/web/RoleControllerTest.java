package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.enums.RoleStatus;
import org.huangyalong.modules.system.request.RoleUtil;
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

@AutoConfigureMockMvc
@IntegrationTest
class RoleControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void add() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = RoleUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(RoleUtil.DEFAULT_NAME);
        assertThat(testEntity.getCode())
                .isEqualTo(RoleUtil.DEFAULT_CODE);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(RoleUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(RoleStatus.TYPE0);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.put()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO(RoleUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = RoleUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(RoleUtil.UPDATED_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(RoleUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(RoleStatus.TYPE0);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/role/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[*].name")
                .value(hasItem(RoleUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[*].code")
                .value(hasItem(RoleUtil.DEFAULT_CODE))
                .jsonPath("$.data.list.[*].desc")
                .value(hasItem(RoleUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[*].status")
                .value(hasItem(RoleStatus.TYPE0.getValue()));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/role/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[*].name")
                .value(hasItem(RoleUtil.DEFAULT_NAME))
                .jsonPath("$.data.[*].code")
                .value(hasItem(RoleUtil.DEFAULT_CODE))
                .jsonPath("$.data.[*].desc")
                .value(hasItem(RoleUtil.DEFAULT_DESC))
                .jsonPath("$.data.[*].status")
                .value(hasItem(RoleStatus.TYPE0.getValue()));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void items() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/role/_items")
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
                .value(hasItem(RoleUtil.DEFAULT_NAME));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void getById() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/role/{id:.+}", RoleUtil.getId())
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
                .value(is(RoleUtil.DEFAULT_NAME))
                .jsonPath("$.data.code")
                .value(is(RoleUtil.DEFAULT_CODE))
                .jsonPath("$.data.desc")
                .value(is(RoleUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(RoleStatus.TYPE0.getValue()));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(7)
    @Test
    void delete() {
        var beforeSize = Role.create()
                .count();
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.delete()
                .uri("/role/{id:.+}", RoleUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Role.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

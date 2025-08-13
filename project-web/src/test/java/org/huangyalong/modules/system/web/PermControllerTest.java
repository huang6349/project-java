package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.enums.PermStatus;
import org.huangyalong.modules.system.request.PermUtil;
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
class PermControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void add() {
        var beforeSize = Perm.create()
                .count();
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
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = PermUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(PermUtil.DEFAULT_NAME);
        assertThat(testEntity.getCode())
                .isEqualTo(PermUtil.DEFAULT_CODE);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(PermUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(PermStatus.TYPE0);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = Perm.create()
                .count();
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
        testClient.put()
                .uri("/perm")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PermUtil.createBO(PermUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = PermUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(PermUtil.UPDATED_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(PermUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(PermStatus.TYPE0);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = Perm.create()
                .count();
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
        testClient.get()
                .uri("/perm/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
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
                .value(hasItem(PermUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[*].code")
                .value(hasItem(PermUtil.DEFAULT_CODE))
                .jsonPath("$.data.list.[*].desc")
                .value(hasItem(PermUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[*].status")
                .value(hasItem(PermStatus.TYPE0.getValue()));
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = Perm.create()
                .count();
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
        testClient.get()
                .uri("/perm/_query")
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
                .value(hasItem(PermUtil.DEFAULT_NAME))
                .jsonPath("$.data.[*].code")
                .value(hasItem(PermUtil.DEFAULT_CODE))
                .jsonPath("$.data.[*].desc")
                .value(hasItem(PermUtil.DEFAULT_DESC))
                .jsonPath("$.data.[*].status")
                .value(hasItem(PermStatus.TYPE0.getValue()));
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void items() {
        var beforeSize = Perm.create()
                .count();
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
        testClient.get()
                .uri("/perm/_items")
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
                .value(hasItem(PermUtil.DEFAULT_NAME));
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void getById() {
        var beforeSize = Perm.create()
                .count();
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
        testClient.get()
                .uri("/perm/{id:.+}", PermUtil.getId())
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
                .value(is(PermUtil.DEFAULT_NAME))
                .jsonPath("$.data.code")
                .value(is(PermUtil.DEFAULT_CODE))
                .jsonPath("$.data.desc")
                .value(is(PermUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(PermStatus.TYPE0.getValue()));
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(7)
    @Test
    void delete() {
        var beforeSize = Perm.create()
                .count();
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
        testClient.delete()
                .uri("/perm/{id:.+}", PermUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Perm.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

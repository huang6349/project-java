package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.enums.PermStatus;
import org.huangyalong.modules.system.request.PermUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.core.enums.IsDeleted;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;

@AutoConfigureMockMvc
@IntegrationTest
class PermControllerTest extends MyFrameworkTest {

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
        var testPerm = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .one();
        assertThat(testPerm)
                .isNotNull();
        assertThat(testPerm.getId())
                .isNotNull();
        assertThat(testPerm.getName())
                .isEqualTo(PermUtil.DEFAULT_NAME);
        assertThat(testPerm.getCode())
                .isEqualTo(PermUtil.DEFAULT_CODE);
        assertThat(testPerm.getCode())
                .isNotNull();
        assertThat(testPerm.getConfigs())
                .isNull();
        assertThat(testPerm.getExtras())
                .isNull();
        assertThat(testPerm.getDesc())
                .isEqualTo(PermUtil.DEFAULT_DESC);
        assertThat(testPerm.getStatus())
                .isEqualTo(PermStatus.TYPE0);
        assertThat(testPerm.getCreateTime())
                .isNotNull();
        assertThat(testPerm.getUpdateTime())
                .isNotNull();
        assertThat(testPerm.getVersion())
                .isNotNull();
        assertThat(testPerm.getIsDeleted())
                .isEqualTo(IsDeleted.TYPE0);
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
        var perm = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .one();
        assertThat(perm)
                .isNotNull();
        assertThat(perm.getId())
                .isNotNull();
        testClient.put()
                .uri("/perm")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PermUtil.createBO(perm.getId()))
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
        var testPerm = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .one();
        assertThat(testPerm)
                .isNotNull();
        assertThat(testPerm.getId())
                .isNotNull();
        assertThat(testPerm.getName())
                .isEqualTo(PermUtil.UPDATED_NAME);
        assertThat(testPerm.getCode())
                .isNotNull();
        assertThat(testPerm.getConfigs())
                .isNull();
        assertThat(testPerm.getExtras())
                .isNull();
        assertThat(testPerm.getDesc())
                .isEqualTo(PermUtil.UPDATED_DESC);
        assertThat(testPerm.getStatus())
                .isEqualTo(PermStatus.TYPE0);
        assertThat(testPerm.getCreateTime())
                .isNotNull();
        assertThat(testPerm.getUpdateTime())
                .isNotNull();
        assertThat(testPerm.getVersion())
                .isNotNull();
        assertThat(testPerm.getIsDeleted())
                .isEqualTo(IsDeleted.TYPE0);
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
        var perm = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .one();
        assertThat(perm)
                .isNotNull();
        assertThat(perm.getId())
                .isNotNull();
        testClient.get()
                .uri("/perm/{id:.+}", perm.getId())
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

    @Order(6)
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
        var perm = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .one();
        assertThat(perm)
                .isNotNull();
        assertThat(perm.getId())
                .isNotNull();
        testClient.delete()
                .uri("/perm/{id:.+}", perm.getId())
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

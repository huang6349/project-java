package org.huangyalong.modules.example.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.example.domain.Example;
import org.huangyalong.modules.example.enums.ExampleStatus;
import org.huangyalong.modules.example.request.ExampleUtil;
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
import static org.huangyalong.modules.example.domain.table.ExampleTableDef.EXAMPLE;

@AutoConfigureMockMvc
@IntegrationTest
class ExampleControllerTest extends MyFrameworkTest {

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
        var beforeSize = new Example()
                .count();
        testClient.post()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = new Example()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testExample = new Example()
                .orderBy(EXAMPLE.ID, Boolean.FALSE)
                .one();
        assertThat(testExample)
                .isNotNull();
        assertThat(testExample.getId())
                .isNotNull();
        assertThat(testExample.getName())
                .isEqualTo(ExampleUtil.DEFAULT_NAME);
        assertThat(testExample.getCode())
                .isNotNull();
        assertThat(testExample.getConfigs())
                .isNull();
        assertThat(testExample.getExtras())
                .isNull();
        assertThat(testExample.getDesc())
                .isEqualTo(ExampleUtil.DEFAULT_DESC);
        assertThat(testExample.getStatus())
                .isEqualTo(ExampleStatus.TYPE0);
        assertThat(testExample.getCreateTime())
                .isNotNull();
        assertThat(testExample.getUpdateTime())
                .isNotNull();
        assertThat(testExample.getVersion())
                .isNotNull();
        assertThat(testExample.getIsDeleted())
                .isEqualTo(IsDeleted.TYPE0);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = new Example()
                .count();
        testClient.post()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var example = new Example()
                .orderBy(EXAMPLE.ID, Boolean.FALSE)
                .one();
        assertThat(example)
                .isNotNull();
        assertThat(example.getId())
                .isNotNull();
        testClient.put()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO(example.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = new Example()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testExample = new Example()
                .orderBy(EXAMPLE.ID, Boolean.FALSE)
                .one();
        assertThat(testExample)
                .isNotNull();
        assertThat(testExample.getId())
                .isNotNull();
        assertThat(testExample.getName())
                .isEqualTo(ExampleUtil.UPDATED_NAME);
        assertThat(testExample.getCode())
                .isNotNull();
        assertThat(testExample.getConfigs())
                .isNull();
        assertThat(testExample.getExtras())
                .isNull();
        assertThat(testExample.getDesc())
                .isEqualTo(ExampleUtil.UPDATED_DESC);
        assertThat(testExample.getStatus())
                .isEqualTo(ExampleStatus.TYPE0);
        assertThat(testExample.getCreateTime())
                .isNotNull();
        assertThat(testExample.getUpdateTime())
                .isNotNull();
        assertThat(testExample.getVersion())
                .isNotNull();
        assertThat(testExample.getIsDeleted())
                .isEqualTo(IsDeleted.TYPE0);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = new Example()
                .count();
        testClient.post()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/example/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.records.[*].name")
                .value(hasItem(ExampleUtil.DEFAULT_NAME))
                .jsonPath("$.data.records.[*].desc")
                .value(hasItem(ExampleUtil.DEFAULT_DESC))
                .jsonPath("$.data.records.[*].status")
                .value(hasItem(ExampleStatus.TYPE0.getValue()));
        var afterSize = new Example()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = new Example()
                .count();
        testClient.post()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/example/_query")
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
                .value(hasItem(ExampleUtil.DEFAULT_NAME))
                .jsonPath("$.data.[*].desc")
                .value(hasItem(ExampleUtil.DEFAULT_DESC))
                .jsonPath("$.data.[*].status")
                .value(hasItem(ExampleStatus.TYPE0.getValue()));
        var afterSize = new Example()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = new Example()
                .count();
        testClient.post()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var example = new Example()
                .orderBy(EXAMPLE.ID, Boolean.FALSE)
                .one();
        assertThat(example)
                .isNotNull();
        assertThat(example.getId())
                .isNotNull();
        testClient.get()
                .uri("/example/{id:.+}", example.getId())
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
                .value(is(ExampleUtil.DEFAULT_NAME))
                .jsonPath("$.data.desc")
                .value(is(ExampleUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(ExampleStatus.TYPE0.getValue()));
        var afterSize = new Example()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
        var beforeSize = new Example()
                .count();
        testClient.post()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var example = new Example()
                .orderBy(EXAMPLE.ID, Boolean.FALSE)
                .one();
        assertThat(example)
                .isNotNull();
        assertThat(example.getId())
                .isNotNull();
        testClient.delete()
                .uri("/example/{id:.+}", example.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = new Example()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

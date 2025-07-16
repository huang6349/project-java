package org.huangyalong.modules.example.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.example.domain.Example;
import org.huangyalong.modules.example.enums.ExampleStatus;
import org.huangyalong.modules.example.request.ExampleUtil;
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
class ExampleControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void add() {
        var beforeSize = Example.create()
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
        var afterSize = Example.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = ExampleUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(ExampleUtil.DEFAULT_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(ExampleUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(ExampleStatus.TYPE0);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = Example.create()
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
        testClient.put()
                .uri("/example")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleUtil.createBO(ExampleUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Example.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = ExampleUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(ExampleUtil.UPDATED_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(ExampleUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(ExampleStatus.TYPE0);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = Example.create()
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
                .jsonPath("$.data.list.[0].name")
                .value(is(ExampleUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[0].desc")
                .value(is(ExampleUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[0].status")
                .value(is(ExampleStatus.TYPE0.getValue()));
        var afterSize = Example.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = Example.create()
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
                .jsonPath("$.data.[0].name")
                .value(is(ExampleUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].desc")
                .value(is(ExampleUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(ExampleStatus.TYPE0.getValue()));
        var afterSize = Example.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = Example.create()
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
                .uri("/example/{id:.+}", ExampleUtil.getId())
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
        var afterSize = Example.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
        var beforeSize = Example.create()
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
        testClient.delete()
                .uri("/example/{id:.+}", ExampleUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Example.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

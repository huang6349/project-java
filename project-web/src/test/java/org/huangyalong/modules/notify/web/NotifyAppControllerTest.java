package org.huangyalong.modules.notify.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.notify.domain.CategoryConfigs;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.enums.CategoryStatus;
import org.huangyalong.modules.notify.request.AppUtil;
import org.huangyalong.modules.notify.request.CategoryUtil;
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
class NotifyAppControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        testClient.post()
                .uri("/notify/category")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CategoryUtil.createBO())
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
    void add() {
        var beforeSize = NotifyApp.create()
                .count();
        testClient.post()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = NotifyApp.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = AppUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getCategoryId())
                .isEqualTo(CategoryUtil.getId());
        assertThat(testEntity.getName())
                .isEqualTo(AppUtil.DEFAULT_NAME);
        assertThat(testEntity.getCode())
                .isNotEmpty();
        assertThat(testEntity.getMetadata())
                .isNull();
        assertThat(testEntity.getConfigs())
                .hasSize(2);
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(AppUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(CategoryStatus.TYPE0);
        var configs = testEntity.getConfigs();
        var testConfigs = JSONUtil.parseObj(configs);
        assertThat(testConfigs.getRaw())
                .isNotNull();
        assertThat(testConfigs.getRaw())
                .hasSize(2);
        assertThat(testConfigs.getByPath(CategoryConfigs.NAME_FREQ))
                .isEqualTo(AppUtil.DEFAULT_FREQ);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = NotifyApp.create()
                .count();
        testClient.post()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.put()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO(AppUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = NotifyApp.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = AppUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(AppUtil.UPDATED_NAME);
        assertThat(testEntity.getCode())
                .isNotEmpty();
        assertThat(testEntity.getMetadata())
                .isNull();
        assertThat(testEntity.getConfigs())
                .hasSize(2);
        assertThat(testEntity.getExtras())
                .isNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(AppUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(CategoryStatus.TYPE0);
        var configs = testEntity.getConfigs();
        var testConfigs = JSONUtil.parseObj(configs);
        assertThat(testConfigs.getRaw())
                .isNotNull();
        assertThat(testConfigs.getRaw())
                .hasSize(2);
        assertThat(testConfigs.getByPath(CategoryConfigs.NAME_FREQ))
                .isEqualTo(AppUtil.UPDATED_FREQ);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = NotifyApp.create()
                .count();
        testClient.post()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/notify/app/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
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
                .value(is(AppUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[0].code")
                .isNotEmpty()
                .jsonPath("$.data.list.[0].freq")
                .value(is(AppUtil.DEFAULT_FREQ))
                .jsonPath("$.data.list.[0].desc")
                .value(is(AppUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[0].status")
                .value(is(CategoryStatus.TYPE0.getValue()));
        var afterSize = NotifyApp.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = NotifyApp.create()
                .count();
        testClient.post()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/notify/app/_query")
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
                .value(is(AppUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].code")
                .isNotEmpty()
                .jsonPath("$.data.[0].freq")
                .value(is(AppUtil.DEFAULT_FREQ))
                .jsonPath("$.data.[0].desc")
                .value(is(AppUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(CategoryStatus.TYPE0.getValue()));
        var afterSize = NotifyApp.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = NotifyApp.create()
                .count();
        testClient.post()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/notify/app/{id:.+}", AppUtil.getId())
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
                .value(is(AppUtil.DEFAULT_NAME))
                .jsonPath("$.data.code")
                .isNotEmpty()
                .jsonPath("$.data.freq")
                .value(is(AppUtil.DEFAULT_FREQ))
                .jsonPath("$.data.desc")
                .value(is(AppUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(CategoryStatus.TYPE0.getValue()));
        var afterSize = NotifyApp.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
        var beforeSize = NotifyApp.create()
                .count();
        testClient.post()
                .uri("/notify/app")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AppUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.delete()
                .uri("/notify/app/{id:.+}", AppUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = NotifyApp.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

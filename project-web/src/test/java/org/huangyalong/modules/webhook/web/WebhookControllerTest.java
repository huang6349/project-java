package org.huangyalong.modules.webhook.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.webhook.domain.Webhook;
import org.huangyalong.modules.webhook.enums.WebhookStatus;
import org.huangyalong.modules.webhook.request.WebhookUtil;
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
class WebhookControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void add() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = WebhookUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getUrl())
                .isEqualTo(WebhookUtil.DEFAULT_URL);
        assertThat(testEntity.getDesc())
                .isEqualTo(WebhookUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(WebhookStatus.TYPE0);
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(4);
        assertThat(testExtras.getByPath("format"))
                .isEqualTo(WebhookUtil.DEFAULT_FORMAT);
        assertThat(testExtras.getByPath("secret"))
                .isEqualTo(WebhookUtil.DEFAULT_SECRET);
        assertThat(testExtras.getByPath("trigger"))
                .isEqualTo(WebhookUtil.DEFAULT_TRIGGER);
        assertThat(testExtras.getByPath("version"))
                .isNotNull();
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.put()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO(WebhookUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = WebhookUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getUrl())
                .isEqualTo(WebhookUtil.UPDATED_URL);
        assertThat(testEntity.getDesc())
                .isEqualTo(WebhookUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(WebhookStatus.TYPE0);
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(4);
        assertThat(testExtras.getByPath("format"))
                .isEqualTo(WebhookUtil.UPDATED_FORMAT);
        assertThat(testExtras.getByPath("secret"))
                .isEqualTo(WebhookUtil.UPDATED_SECRET);
        assertThat(testExtras.getByPath("trigger"))
                .isEqualTo(WebhookUtil.UPDATED_TRIGGER);
        assertThat(testExtras.getByPath("version"))
                .isNotNull();
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/webhook/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].url")
                .value(is(WebhookUtil.DEFAULT_URL))
                .jsonPath("$.data.list.[0].format")
                .value(is(WebhookUtil.DEFAULT_FORMAT))
                .jsonPath("$.data.list.[0].secret")
                .value(is(WebhookUtil.DEFAULT_SECRET))
                .jsonPath("$.data.list.[0].trigger")
                .value(is(WebhookUtil.DEFAULT_TRIGGER))
                .jsonPath("$.data.list.[0].desc")
                .value(is(WebhookUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[0].status")
                .value(is(WebhookStatus.TYPE0.getValue()));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/webhook/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].url")
                .value(is(WebhookUtil.DEFAULT_URL))
                .jsonPath("$.data.[0].format")
                .value(is(WebhookUtil.DEFAULT_FORMAT))
                .jsonPath("$.data.[0].secret")
                .value(is(WebhookUtil.DEFAULT_SECRET))
                .jsonPath("$.data.[0].trigger")
                .value(is(WebhookUtil.DEFAULT_TRIGGER))
                .jsonPath("$.data.[0].desc")
                .value(is(WebhookUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(WebhookStatus.TYPE0.getValue()));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/webhook/{id:.+}", WebhookUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.url")
                .value(is(WebhookUtil.DEFAULT_URL))
                .jsonPath("$.data.format")
                .value(is(WebhookUtil.DEFAULT_FORMAT))
                .jsonPath("$.data.secret")
                .value(is(WebhookUtil.DEFAULT_SECRET))
                .jsonPath("$.data.trigger")
                .value(is(WebhookUtil.DEFAULT_TRIGGER))
                .jsonPath("$.data.desc")
                .value(is(WebhookUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(WebhookStatus.TYPE0.getValue()));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void test() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.post()
                .uri("/webhook/{id:.+}/_test", WebhookUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data")
                .value(is(Boolean.TRUE));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(7)
    @Test
    void delete() {
        var beforeSize = new Webhook()
                .count();
        testClient.post()
                .uri("/webhook")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(WebhookUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.delete()
                .uri("/webhook/{id:.+}", WebhookUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = new Webhook()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

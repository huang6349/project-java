package org.huangyalong.modules.ai.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.ai.domain.AiDocument;
import org.huangyalong.modules.ai.domain.AiDocumentExtras;
import org.huangyalong.modules.ai.enums.AiDocumentStatus;
import org.huangyalong.modules.ai.request.AiDocumentUtil;
import org.huangyalong.modules.file.request.FileUtil;
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
class AiDocumentControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        testClient.post()
                .uri("/file/upload")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(FileUtil.createFile())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
    }

    @Order(1)
    @Test
    void add() {
        var beforeSize = AiDocument.create()
                .count();
        testClient.post()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = AiDocument.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = AiDocumentUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(AiDocumentUtil.DEFAULT_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNotNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(AiDocumentUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(AiDocumentStatus.TYPE0);
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(4);
        assertThat(testExtras.getByPath(AiDocumentExtras.NAME_FILENAME))
                .isEqualTo(FileUtil.getFilename());
        assertThat(testExtras.getByPath(AiDocumentExtras.NAME_EXT))
                .isEqualTo(FileUtil.DEFAULT_EXT);
        assertThat(testExtras.getByPath(AiDocumentExtras.NAME_CONTENT_TYPE))
                .isNotNull();
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = AiDocument.create()
                .count();
        testClient.post()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.put()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO(AiDocumentUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = AiDocument.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = AiDocumentUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(AiDocumentUtil.UPDATED_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .isNotNull();
        assertThat(testEntity.getDesc())
                .isEqualTo(AiDocumentUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(AiDocumentStatus.TYPE0);
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(4);
        assertThat(testExtras.getByPath(AiDocumentExtras.NAME_FILENAME))
                .isEqualTo(FileUtil.getFilename());
        assertThat(testExtras.getByPath(AiDocumentExtras.NAME_EXT))
                .isEqualTo(FileUtil.DEFAULT_EXT);
        assertThat(testExtras.getByPath(AiDocumentExtras.NAME_CONTENT_TYPE))
                .isNotNull();
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = AiDocument.create()
                .count();
        testClient.post()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/ai/document/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
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
                .value(is(AiDocumentUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[0].filename")
                .value(is(FileUtil.getFilename()))
                .jsonPath("$.data.list.[0].ext")
                .value(is(FileUtil.DEFAULT_EXT))
                .jsonPath("$.data.list.[0].desc")
                .value(is(AiDocumentUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[0].status")
                .value(is(AiDocumentStatus.TYPE0.getValue()));
        var afterSize = AiDocument.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = AiDocument.create()
                .count();
        testClient.post()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/ai/document/_query")
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
                .value(is(AiDocumentUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].filename")
                .value(is(FileUtil.getFilename()))
                .jsonPath("$.data.[0].ext")
                .value(is(FileUtil.DEFAULT_EXT))
                .jsonPath("$.data.[0].desc")
                .value(is(AiDocumentUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(AiDocumentStatus.TYPE0.getValue()));
        var afterSize = AiDocument.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = AiDocument.create()
                .count();
        testClient.post()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/ai/document/{id:.+}", AiDocumentUtil.getId())
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
                .value(is(AiDocumentUtil.DEFAULT_NAME))
                .jsonPath("$.data.filename")
                .value(is(FileUtil.getFilename()))
                .jsonPath("$.data.ext")
                .value(is(FileUtil.DEFAULT_EXT))
                .jsonPath("$.data.desc")
                .value(is(AiDocumentUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(AiDocumentStatus.TYPE0.getValue()));
        var afterSize = AiDocument.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
        var beforeSize = AiDocument.create()
                .count();
        testClient.post()
                .uri("/ai/document")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AiDocumentUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.delete()
                .uri("/ai/document/{id:.+}", AiDocumentUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = AiDocument.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}

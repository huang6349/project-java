package org.huangyalong.modules.ai.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.ai.domain.AiChunk;
import org.huangyalong.modules.ai.request.AiChunkUtil;
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

import static cn.hutool.core.convert.Convert.toStr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@IntegrationTest
class AiChunkControllerTest extends MyFrameworkTest {

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
    void queryPage() {
        var beforeSize = AiChunk.create()
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
                .uri("/ai/document/chunk/_query/paging?pageSize={pageSize}&documentId={documentId}", Long.MAX_VALUE, AiDocumentUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].documentId")
                .value(is(toStr(AiDocumentUtil.getId())))
                .jsonPath("$.data.list.[0].content")
                .value(is(FileUtil.DEFAULT_CONTENT));
        var afterSize = AiChunk.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(2)
    @Test
    void query() {
        var beforeSize = AiChunk.create()
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
                .uri("/ai/document/chunk/_query?documentId={documentId}", AiDocumentUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].documentId")
                .value(is(toStr(AiDocumentUtil.getId())))
                .jsonPath("$.data.[0].content")
                .value(is(FileUtil.DEFAULT_CONTENT));
        var afterSize = AiChunk.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(3)
    @Test
    void getById() {
        var beforeSize = AiChunk.create()
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
                .uri("/ai/document/chunk/{id:.+}", AiChunkUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.documentId")
                .value(is(toStr(AiDocumentUtil.getId())))
                .jsonPath("$.data.content")
                .value(is(FileUtil.DEFAULT_CONTENT));
        var afterSize = AiChunk.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }
}

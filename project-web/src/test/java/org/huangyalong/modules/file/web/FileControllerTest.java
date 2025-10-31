package org.huangyalong.modules.file.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.file.domain.File;
import org.huangyalong.modules.file.enums.FileStatus;
import org.huangyalong.modules.file.request.FileUtil;
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
class FileControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void upload() {
        var beforeSize = File.create()
                .count();
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
        var afterSize = File.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = FileUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getFilename())
                .isNotNull();
        assertThat(testEntity.getOrigFilename())
                .isEqualTo(FileUtil.DEFAULT_ORIG_FILENAME);
        assertThat(testEntity.getExt())
                .isEqualTo(FileUtil.DEFAULT_EXT);
        assertThat(testEntity.getStatus())
                .isEqualTo(FileStatus.TYPE0);
    }

    @Order(2)
    @Test
    void queryPage() {
        var beforeSize = File.create()
                .count();
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
        testClient.get()
                .uri("/file/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].origFilename")
                .value(is(FileUtil.DEFAULT_ORIG_FILENAME))
                .jsonPath("$.data.list.[0].ext")
                .value(is(FileUtil.DEFAULT_EXT))
                .jsonPath("$.data.list.[0].status")
                .value(is(FileStatus.TYPE0.getValue()));
        var afterSize = File.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(3)
    @Test
    void query() {
        var beforeSize = File.create()
                .count();
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
        testClient.get()
                .uri("/file/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].origFilename")
                .value(is(FileUtil.DEFAULT_ORIG_FILENAME))
                .jsonPath("$.data.[0].ext")
                .value(is(FileUtil.DEFAULT_EXT))
                .jsonPath("$.data.[0].status")
                .value(is(FileStatus.TYPE0.getValue()));
        var afterSize = File.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void getById() {
        var beforeSize = File.create()
                .count();
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
        testClient.get()
                .uri("/file/{id:.+}", FileUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.origFilename")
                .value(is(FileUtil.DEFAULT_ORIG_FILENAME))
                .jsonPath("$.data.ext")
                .value(is(FileUtil.DEFAULT_EXT))
                .jsonPath("$.data.status")
                .value(is(FileStatus.TYPE0.getValue()));
        var afterSize = File.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }
}

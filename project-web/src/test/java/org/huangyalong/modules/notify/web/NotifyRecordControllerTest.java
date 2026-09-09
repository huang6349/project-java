package org.huangyalong.modules.notify.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.notify.request.CategoryUtil;
import org.huangyalong.modules.notify.request.RecordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.is;
import static org.huangyalong.extra.notify.helper.NotifyHelper.DEFAULT_APP;
import static org.huangyalong.extra.notify.helper.NotifyHelper.DEFAULT_APP_NAME;
import static org.huangyalong.modules.notify.enums.NotifyStatus.TYPE1;

@AutoConfigureMockMvc
@IntegrationTest
class NotifyRecordControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        testClient.post()
                .uri("/notify/category")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CategoryUtil.createBO("Console"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        RecordUtil.writeTestData();
    }

    @Order(1)
    @Test
    void queryAfter() {
        testClient.get()
                .uri("/notify/record/_query/after?pageSize={pageSize}", 10)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].recipient")
                .value(is(RecordUtil.DEFAULT_RECIPIENT))
                .jsonPath("$.data.list.[0].channel")
                .isNotEmpty()
                .jsonPath("$.data.list.[0].code")
                .value(is(RecordUtil.DEFAULT_CODE))
                .jsonPath("$.data.list.[0].app")
                .value(is(DEFAULT_APP))
                .jsonPath("$.data.list.[0].status")
                .value(is(TYPE1.getValue()))
                .jsonPath("$.data.list.[0].name")
                .value(is(CategoryUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[0].appName")
                .value(is(DEFAULT_APP_NAME))
                .jsonPath("$.data.list.[0].content")
                .value(is(RecordUtil.DEFAULT_CONTENT));
    }

    @Order(2)
    @Test
    void query() {
        testClient.get()
                .uri("/notify/record/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].recipient")
                .value(is(RecordUtil.DEFAULT_RECIPIENT))
                .jsonPath("$.data.[0].channel")
                .isNotEmpty()
                .jsonPath("$.data.[0].code")
                .value(is(RecordUtil.DEFAULT_CODE))
                .jsonPath("$.data.[0].app")
                .value(is(DEFAULT_APP))
                .jsonPath("$.data.[0].status")
                .value(is(TYPE1.getValue()))
                .jsonPath("$.data.[0].name")
                .value(is(CategoryUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].appName")
                .value(is(DEFAULT_APP_NAME))
                .jsonPath("$.data.[0].content")
                .value(is(RecordUtil.DEFAULT_CONTENT));
    }

    @Order(3)
    @Test
    void last() {
        testClient.get()
                .uri("/notify/record/_query/last")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.recipient")
                .value(is(RecordUtil.DEFAULT_RECIPIENT))
                .jsonPath("$.data.channel")
                .isNotEmpty()
                .jsonPath("$.data.code")
                .value(is(RecordUtil.DEFAULT_CODE))
                .jsonPath("$.data.app")
                .value(is(DEFAULT_APP))
                .jsonPath("$.data.status")
                .value(is(TYPE1.getValue()))
                .jsonPath("$.data.name")
                .value(is(CategoryUtil.DEFAULT_NAME))
                .jsonPath("$.data.appName")
                .value(is(DEFAULT_APP_NAME))
                .jsonPath("$.data.content")
                .value(is(RecordUtil.DEFAULT_CONTENT));
    }

    @Order(4)
    @Test
    void getById() {
        testClient.get()
                .uri("/notify/record/{id:.+}", RecordUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.recipient")
                .value(is(RecordUtil.DEFAULT_RECIPIENT))
                .jsonPath("$.data.channel")
                .isNotEmpty()
                .jsonPath("$.data.code")
                .value(is(RecordUtil.DEFAULT_CODE))
                .jsonPath("$.data.app")
                .value(is(DEFAULT_APP))
                .jsonPath("$.data.status")
                .value(is(TYPE1.getValue()))
                .jsonPath("$.data.name")
                .value(is(CategoryUtil.DEFAULT_NAME))
                .jsonPath("$.data.appName")
                .value(is(DEFAULT_APP_NAME))
                .jsonPath("$.data.content")
                .value(is(RecordUtil.DEFAULT_CONTENT));
    }
}

package org.huangyalong.extra.notify.processor;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.notify.request.CategoryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.extra.notify.NotifyData;
import org.myframework.extra.notify.NotifyMessage;
import org.myframework.extra.notify.NotifySendHelper;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.is;

/**
 * 控制台渠道处理器测试
 * <p>
 * 冒烟:SPI 装配 -> 负载解析 -> 限频 -> 渠道发送 -> 频次 -> QuestDB 回写 全链不抛即通过。
 * 类别 code=Console 为回写硬依赖(tple 固定),一次性数据
 */
@AutoConfigureMockMvc
@IntegrationTest
class ConsoleProcessorTest extends MyFrameworkTest {

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
    }

    @Order(1)
    @Test
    void send() {
        var message = new NotifyMessage();
        message.addData(NotifyData.of("recipient", "13800138000"));
        message.addData(NotifyData.of("id", "20260909101428003"));
        message.addData(NotifyData.of("content", "控制台渠道测试消息"));
        NotifySendHelper.send(ConsoleProcessor.class, message);
    }
}

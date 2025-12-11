package org.myframework.webhook.log;

import cn.hutool.log.StaticLog;
import org.myframework.webhook.WebhookLogService;
import org.myframework.webhook.WebhookRequest;

public class SimpleWebhookLogService implements WebhookLogService {

    @Override
    public void logSuccess(WebhookRequest request, String deliveryId) {
        StaticLog.info("Webhook 请求成功 - URL: {}, 事件类型: {}, 请求编号: {}",
                request.getUrl(),
                request.getEventType(),
                deliveryId);
    }

    @Override
    public void logFailure(WebhookRequest request, String deliveryId) {
        StaticLog.error("Webhook 请求失败 - URL: {}, 事件类型: {}, 请求编号: {}",
                request.getUrl(),
                request.getEventType(),
                deliveryId);
    }

    @Override
    public void logStart(WebhookRequest request, String deliveryId) {
        StaticLog.debug("Webhook 请求开始 - URL: {}, 事件类型: {}, 请求编号: {}",
                request.getUrl(),
                request.getEventType(),
                deliveryId);
    }
}

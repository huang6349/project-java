package org.myframework.extra.webhook;

public interface WebhookLogService {

    void logSuccess(WebhookRequest request, String deliveryId);

    void logFailure(WebhookRequest request, String deliveryId);

    void logStart(WebhookRequest request, String deliveryId);
}

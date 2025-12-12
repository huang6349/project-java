package org.myframework.extra.webhook;

public interface WebhookConstants {

    String HEADER_WEBHOOK_EVENT = "X-Webhook-Event";

    String HEADER_WEBHOOK_SIGNATURE = "X-Webhook-Signature";

    String HEADER_WEBHOOK_DELIVERY = "X-Webhook-Delivery";

    String HEADER_WEBHOOK_VERSION = "X-Webhook-Version";

    String HEADER_WEBHOOK_TIMESTAMP = "X-Webhook-Timestamp";

    String HEADER_USER_AGENT = "User-Agent";

    String USER_AGENT = "Webhook-Service/1.0";
}

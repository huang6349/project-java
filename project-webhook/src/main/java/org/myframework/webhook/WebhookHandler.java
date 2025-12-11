package org.myframework.webhook;

import cn.hutool.json.JSONObject;

import java.util.List;

import static cn.hutool.core.lang.Opt.ofNullable;

public interface WebhookHandler {

    List<WebhookRequest> buildRequests(WebhookMessage message);

    Object buildData(JSONObject data);

    boolean isSupported(String eventType);

    default Object buildData(WebhookMessage message) {
        return buildData(ofNullable(message)
                .map(WebhookMessage::getData)
                .map(JSONObject::new)
                .get());
    }
}

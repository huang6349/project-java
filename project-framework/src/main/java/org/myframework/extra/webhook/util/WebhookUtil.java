package org.myframework.extra.webhook.util;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.util.Map;

import static cn.hutool.core.convert.Convert.toStr;
import static cn.hutool.core.date.DateUtil.current;
import static cn.hutool.core.util.CharsetUtil.CHARSET_UTF_8;
import static cn.hutool.http.ContentType.FORM_URLENCODED;
import static cn.hutool.http.ContentType.JSON;
import static cn.hutool.json.JSONUtil.parseObj;
import static cn.hutool.json.JSONUtil.toJsonStr;
import static org.myframework.extra.webhook.WebhookConstants.*;

public class WebhookUtil {

    public static HttpResponse sendJsonWebhook(String url,
                                               Object body,
                                               String eventType,
                                               String signature,
                                               String deliveryId,
                                               Map<String, String> headers,
                                               int timeout) {
        var request = HttpRequest.post(url)
                .header(HEADER_WEBHOOK_EVENT, eventType)
                .header(HEADER_WEBHOOK_SIGNATURE, signature)
                .header(HEADER_WEBHOOK_DELIVERY, deliveryId)
                .header(HEADER_WEBHOOK_VERSION, "1.0")
                .header(HEADER_WEBHOOK_TIMESTAMP, toStr(current()))
                .header(HEADER_USER_AGENT, USER_AGENT)
                .addHeaders(headers)
                .contentType(ContentType.build(JSON, CHARSET_UTF_8))
                .timeout(timeout)
                .body(toJsonStr(body));
        return request.execute();
    }

    public static HttpResponse sendFormWebhook(String url,
                                               Object body,
                                               String eventType,
                                               String signature,
                                               String deliveryId,
                                               Map<String, String> headers,
                                               int timeout) {
        var request = HttpRequest.post(url)
                .header(HEADER_WEBHOOK_EVENT, eventType)
                .header(HEADER_WEBHOOK_SIGNATURE, signature)
                .header(HEADER_WEBHOOK_DELIVERY, deliveryId)
                .header(HEADER_WEBHOOK_VERSION, "1.0")
                .header(HEADER_WEBHOOK_TIMESTAMP, toStr(current()))
                .header(HEADER_USER_AGENT, USER_AGENT)
                .addHeaders(headers)
                .contentType(ContentType.build(FORM_URLENCODED, CHARSET_UTF_8))
                .timeout(timeout);
        if (body instanceof Map) {
            request.form((Map<String, Object>) body);
        } else if (body instanceof String bodyStr) {
            if (JSONUtil.isTypeJSON(bodyStr)) {
                request.form(parseObj(bodyStr));
            } else request.body(bodyStr);
        } else request.form(parseObj(body));
        return request.execute();
    }
}

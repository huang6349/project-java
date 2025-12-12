package org.myframework.extra.webhook.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import org.myframework.core.exception.BusinessException;
import org.myframework.core.exception.ErrorCode;
import org.myframework.extra.dict.EnumDict;
import org.myframework.extra.webhook.*;

import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.core.thread.GlobalThreadPool.execute;
import static cn.hutool.core.util.ObjectUtil.equal;
import static org.myframework.extra.webhook.helper.WebhookHandlerHelper.getHandlers;
import static org.myframework.extra.webhook.util.SignatureUtil.generateSignature;
import static org.myframework.extra.webhook.util.WebhookUtil.sendFormWebhook;
import static org.myframework.extra.webhook.util.WebhookUtil.sendJsonWebhook;

public class WebhookHelper extends AbstractWebhookHelper {

    public static WebhookResponse sendWebhook(WebhookRequest request,
                                              String deliveryId) {
        try {
            // 1. 参数校验
            var url = ofNullable(request)
                    .map(WebhookRequest::getUrl)
                    .orElseThrow(() -> new BusinessException("Webhook 请求地址不能为空"));
            var eventType = ofNullable(request)
                    .map(WebhookRequest::getEventType)
                    .orElseThrow(() -> new BusinessException("Webhook 事件类型不能为空"));
            var format = ofNullable(request)
                    .map(WebhookRequest::getFormat)
                    .orElseThrow(() -> new BusinessException("Webhook 数据格式不能为空"));
            // 2. 记录请求开始日志
            logRequestStart(request, deliveryId);
            // 3. 构建请求参数
            var signature = generateSignature(request, deliveryId);
            var headers = request.getHeaders();
            var timeout = ofNullable(request)
                    .map(WebhookRequest::getTimeout)
                    .orElse(30000);
            var requestBody = request.getData();
            // 4. 根据数据格式发送请求
            var response = equal(WebhookFormat.TYPE0, format)
                    ? sendJsonWebhook(
                    url,
                    requestBody,
                    eventType,
                    signature,
                    deliveryId,
                    headers,
                    timeout)
                    : sendFormWebhook(
                    url,
                    requestBody,
                    eventType,
                    signature,
                    deliveryId,
                    headers,
                    timeout);
            // 5. 处理响应结果
            var webhookResponse = processResponse(response, deliveryId);
            // 6. 根据响应结果记录日志
            if (webhookResponse.getSuccess()) {
                logRequestSuccess(request, deliveryId);
            } else logRequestFailure(request, deliveryId);
            return webhookResponse;
        } catch (BusinessException e) {
            // 业务异常处理：参数校验失败
            logRequestFailure(request, deliveryId);
            return WebhookResponse.builder()
                    .success(Boolean.FALSE)
                    .message(e.getMessage())
                    .code(ErrorCode.BAD_REQUEST.getCode())
                    .build();
        } catch (Exception e) {
            // 系统异常处理：错误的请求
            logRequestFailure(request, deliveryId);
            return WebhookResponse.builder()
                    .success(Boolean.FALSE)
                    .message(ErrorCode.BAD_REQUEST.getMessage())
                    .code(ErrorCode.BAD_REQUEST.getCode())
                    .build();
        }
    }

    public static WebhookResponse sendWebhook(WebhookRequest request) {
        var deliveryId = IdUtil.fastSimpleUUID();
        return sendWebhook(request, deliveryId);
    }

    public static void triggerEvent(WebhookEvent event,
                                    Object data) {
        // 1. 事件类型校验
        var eventType = ofNullable(event)
                .map(EnumDict::getValue)
                .orElseThrow(() -> new BusinessException("Webhook 触发事件不能为空"));
        // 2. 获取事件对应的处理器
        var handlers = getHandlers(eventType);
        // 3. 如果有处理器，则异步发送Webhook
        if (CollUtil.isNotEmpty(handlers)) {
            var eventId = IdUtil.fastSimpleUUID();
            var webhookMessage = WebhookMessage.builder()
                    .eventType(eventType)
                    .data(data)
                    .eventId(eventId)
                    .build();
            handlers.forEach(handler -> execute(() -> {
                var requests = handler.buildRequests(webhookMessage);
                if (CollUtil.isNotEmpty(requests)) {
                    requests.forEach(WebhookHelper::sendWebhook);
                }
            }));
        }
    }
}

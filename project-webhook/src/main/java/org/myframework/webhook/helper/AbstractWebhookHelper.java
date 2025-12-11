package org.myframework.webhook.helper;

import cn.hutool.http.HttpResponse;
import cn.hutool.log.StaticLog;
import org.myframework.webhook.WebhookLogService;
import org.myframework.webhook.WebhookRequest;
import org.myframework.webhook.WebhookResponse;
import org.myframework.webhook.log.SimpleWebhookLogService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import static cn.hutool.core.util.BooleanUtil.isFalse;
import static cn.hutool.extra.spring.SpringUtil.getBean;

public abstract class AbstractWebhookHelper {

    private static volatile WebhookLogService logService;

    protected static WebhookLogService getLog() {
        if (logService == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractWebhookHelper.class) { // 同步锁
                if (logService == null) { // 第二次检查，确保只初始化一次
                    try {
                        logService = getBean(WebhookLogService.class);
                        StaticLog.trace("初始化完成，日志服务已注入");
                    } catch (NoSuchBeanDefinitionException e) {
                        logService = new SimpleWebhookLogService();
                        StaticLog.trace("初始化完成，日志服务已注入");
                    } catch (Exception e) {
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new RuntimeException("初始化失败", e);
                    }
                }
            }
        }
        return logService;
    }

    protected static WebhookResponse processResponse(HttpResponse response,
                                                     String deliveryId) {
        var message = isFalse(response.isOk())
                ? response.body()
                : null;
        return WebhookResponse.builder()
                .success(response.isOk())
                .data(response.body())
                .message(message)
                .code(response.getStatus())
                .deliveryId(deliveryId)
                .build();
    }

    protected static void logRequestSuccess(WebhookRequest request,
                                            String deliveryId) {
        getLog().logSuccess(request, deliveryId);
    }

    protected static void logRequestFailure(WebhookRequest request,
                                            String deliveryId) {
        getLog().logFailure(request, deliveryId);
    }

    protected static void logRequestStart(WebhookRequest request,
                                          String deliveryId) {
        getLog().logStart(request, deliveryId);
    }
}

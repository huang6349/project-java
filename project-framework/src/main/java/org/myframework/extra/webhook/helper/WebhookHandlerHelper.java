package org.myframework.extra.webhook.helper;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.myframework.extra.webhook.Webhook;
import org.myframework.extra.webhook.WebhookHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.time.Duration.ofHours;

public class WebhookHandlerHelper {

    private static final Cache<String, List<WebhookHandler>> cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(ofHours(1))
            .build();

    private static final AtomicBoolean initialized = new AtomicBoolean(Boolean.FALSE);

    private static volatile List<WebhookHandler> handlers;

    public static List<WebhookHandler> getHandlers() {
        if (!initialized.get()) {
            synchronized (WebhookHandlerHelper.class) {
                if (!initialized.get()) {
                    refresh();
                    initialized.set(Boolean.TRUE);
                }
            }
        }
        return handlers;
    }

    public static List<WebhookHandler> getHandlers(String eventType) {
        return cache.get(eventType, e -> getHandlers()
                .stream()
                .filter(handler -> handler.isSupported(e))
                .toList());
    }

    public static synchronized void refresh() {
        handlers = SpringUtil.getBeanFactory()
                .getBeansWithAnnotation(Webhook.class)
                .values()
                .stream()
                .filter(bean -> bean instanceof WebhookHandler)
                .map(WebhookHandler.class::cast)
                .toList();
        cache.invalidateAll();
    }
}

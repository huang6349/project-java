package org.huangyalong.modules.webhook.handler;

import cn.hutool.core.collection.CollUtil;
import org.huangyalong.modules.webhook.domain.Webhook;
import org.myframework.core.exception.BusinessException;
import org.myframework.extra.webhook.WebhookHandler;
import org.myframework.extra.webhook.WebhookMessage;
import org.myframework.extra.webhook.WebhookRequest;

import java.util.List;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.modules.webhook.domain.WebhookExtras.NAME_TRIGGER;
import static org.huangyalong.modules.webhook.domain.table.WebhookTableDef.WEBHOOK;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public abstract class AbstractWebhookHandler implements WebhookHandler {

    @Override
    public List<WebhookRequest> buildRequests(WebhookMessage message) {
        // 校验触发事件
        var eventType = ofNullable(message)
                .map(WebhookMessage::getEventType)
                .orElseThrow(() -> new BusinessException("Webhook 触发事件不能为空"));
        // 查询匹配的webhook配置
        var webhooks = Webhook.create()
                .where(ue(WEBHOOK.EXTRAS, NAME_TRIGGER).eq(eventType))
                .and(ue(WEBHOOK.EXTRAS, NAME_TRIGGER).isNotNull())
                .and(WEBHOOK.URL.isNotNull())
                .list();
        // 无配置直接返回空列表
        var requests = CollUtil.<WebhookRequest>newArrayList();
        if (CollUtil.isEmpty(webhooks))
            throw new BusinessException("未找到触发事件为 {} 的 Webhook 配置", eventType);
        // 构建推送数据
        var data = buildData(message);
        // 为每个webhook构建请求
        for (var webhook : webhooks)
            requests.add(WebhookRequest.builder()
                    .url(webhook.getUrl())
                    .eventType(eventType)
                    .data(data)
                    .format(webhook.getFormat())
                    .secret(webhook.getSecret())
                    .timeout(30000)
                    .build());
        return requests;
    }
}

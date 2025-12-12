package org.huangyalong.modules.webhook.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.huangyalong.modules.webhook.enums.WebhookTrigger;
import org.myframework.core.exception.BusinessException;
import org.myframework.extra.webhook.Webhook;

import static cn.hutool.core.date.DateUtil.current;

@Webhook
public class AlarmWebhookHandler extends AbstractWebhookHandler {

    @Override
    public Object buildData(JSONObject data) {
        if (StrUtil.isBlank(data.getStr("id")))
            throw new BusinessException("缺少必需的参数: id");
        if (StrUtil.isBlank(data.getStr("intrudeAlarm")))
            throw new BusinessException("缺少必需的参数: intrudeAlarm");
        if (StrUtil.isBlank(data.getStr("waterAlarm")))
            throw new BusinessException("缺少必需的参数: waterAlarm");
        data.set("timestamp", current());
        return data;
    }

    @Override
    public boolean isSupported(String event) {
        var events = CollUtil.newArrayList();
        events.add(WebhookTrigger.TYPE0.getValue());
        return CollUtil.contains(events, event);
    }
}

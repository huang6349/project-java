package org.huangyalong.modules.webhook.request;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.webhook.domain.Webhook;
import org.huangyalong.modules.webhook.enums.WebhookTrigger;
import org.myframework.extra.webhook.WebhookFormat;

import java.io.Serializable;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.modules.webhook.domain.table.WebhookTableDef.WEBHOOK;

@UtilityClass
public class WebhookUtil {

    public static final String DEFAULT_URL = "https://jsonplaceholder.typicode.com/posts";
    public static final String DEFAULT_FORMAT = WebhookFormat.TYPE0.getValue();
    public static final String DEFAULT_SECRET = RandomUtil.randomString(12);
    public static final String DEFAULT_TRIGGER = WebhookTrigger.TYPE0.getValue();
    public static final String DEFAULT_DESC = RandomUtil.randomString(12);

    public static final String UPDATED_URL = DEFAULT_URL;
    public static final String UPDATED_FORMAT = WebhookFormat.TYPE1.getValue();
    public static final String UPDATED_SECRET = RandomUtil.randomString(12);
    public static final String UPDATED_TRIGGER = WebhookTrigger.TYPE0.getValue();
    public static final String UPDATED_DESC = RandomUtil.randomString(12);

    public static WebhookBO createBO(JSONObject object) {
        var bo = new WebhookBO();
        bo.setId(object.getLong("id"));
        bo.setUrl(object.getStr("url", DEFAULT_URL));
        bo.setFormat(object.getStr("format", DEFAULT_FORMAT));
        bo.setSecret(object.getStr("secret", DEFAULT_SECRET));
        bo.setTrigger(object.getStr("trigger", DEFAULT_TRIGGER));
        bo.setDesc(object.getStr("desc", DEFAULT_DESC));
        return bo;
    }

    public static WebhookBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("url", UPDATED_URL)
                .set("format", UPDATED_FORMAT)
                .set("secret", UPDATED_SECRET)
                .set("trigger", UPDATED_TRIGGER)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    public static WebhookBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    public static Webhook getEntity() {
        return Webhook.create()
                .orderBy(WEBHOOK.ID, Boolean.FALSE)
                .one();
    }

    public static Long getId() {
        var entity = getEntity();
        return ofNullable(entity)
                .map(Webhook::getId)
                .get();
    }
}

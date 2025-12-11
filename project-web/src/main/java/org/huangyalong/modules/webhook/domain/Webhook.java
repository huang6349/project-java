package org.huangyalong.modules.webhook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;
import org.huangyalong.modules.webhook.enums.WebhookStatus;
import org.huangyalong.modules.webhook.enums.WebhookTrigger;
import org.huangyalong.modules.webhook.request.WebhookBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;
import org.myframework.webhook.WebhookFormat;

import java.util.Map;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "网络钩子")
@Table(value = "tb_webhook")
@Schema(name = "网络钩子")
public class Webhook extends Entity<Webhook, Long> {

    @AutoColumn(comment = "推送地址", notNull = true, length = 512)
    @Schema(description = "推送地址")
    private String url;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "配置信息", type = TEXT)
    @Schema(description = "配置信息")
    private Map<String, Object> configs;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "额外信息", type = TEXT)
    @Schema(description = "额外信息")
    private Map<String, Object> extras;

    @AutoColumn(comment = "备注", length = 512)
    @Schema(description = "备注")
    private String desc;

    @JKDictFormat
    @AutoColumn(comment = "钩子状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "钩子状态")
    private WebhookStatus status;

    /****************** view ******************/

    @JKDictFormat
    @Column(ignore = true)
    @Schema(description = "数据格式")
    private WebhookFormat format;

    @Column(ignore = true)
    @Schema(description = "密钥文本")
    private String secret;

    @JKDictFormat
    @Column(ignore = true)
    @Schema(description = "触发事件")
    private WebhookTrigger trigger;

    /****************** with ******************/

    public Webhook with(WebhookBO webhookBO) {
        ofNullable(webhookBO)
                .map(WebhookBO::getUrl)
                .ifPresent(this::setUrl);
        ofNullable(webhookBO)
                .map(WebhookBO::getDesc)
                .ifPresent(this::setDesc);
        var format = ofNullable(webhookBO)
                .map(WebhookBO::getFormat)
                .get();
        var secret = ofNullable(webhookBO)
                .map(WebhookBO::getSecret)
                .get();
        var trigger = ofNullable(webhookBO)
                .map(WebhookBO::getTrigger)
                .get();
        setExtras(WebhookExtras.create()
                .setExtras(getExtras())
                .addFormat(format)
                .addSecret(secret)
                .addTrigger(trigger)
                .addVersion()
                .getExtras());
        return this;
    }
}

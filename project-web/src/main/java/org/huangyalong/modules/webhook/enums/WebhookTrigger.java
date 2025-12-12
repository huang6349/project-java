package org.huangyalong.modules.webhook.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.myframework.extra.dict.Dict;
import org.myframework.extra.dict.EnumDict;
import org.myframework.extra.webhook.WebhookEvent;

import static org.myframework.extra.dict.IsDefault.YES;
import static org.myframework.extra.dict.Style.DEFAULT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Dict(name = "触发事件")
public enum WebhookTrigger implements WebhookEvent {
    TYPE0("res.alert", "资源告警", 0, YES.getValue(), DEFAULT.getValue());

    @EnumValue
    @JsonValue
    private String value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;

    public static WebhookTrigger fromValue(String value) {
        var enumClass = WebhookTrigger.class;
        return EnumDict.fromValue(value, enumClass);
    }
}

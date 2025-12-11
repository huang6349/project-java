package org.myframework.webhook;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.myframework.extra.dict.Dict;
import org.myframework.extra.dict.EnumDict;

import static org.myframework.extra.dict.IsDefault.NO;
import static org.myframework.extra.dict.IsDefault.YES;
import static org.myframework.extra.dict.Style.DEFAULT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Dict(name = "数据格式")
public enum WebhookFormat implements EnumDict<String> {
    TYPE0("1", "JSON", 0, YES.getValue(), DEFAULT.getValue()),
    TYPE1("2", "表单", 0, NO.getValue(), DEFAULT.getValue());

    @EnumValue
    @JsonValue
    private String value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;

    public static WebhookFormat fromValue(String value) {
        var enumClass = WebhookFormat.class;
        return EnumDict.fromValue(value, enumClass);
    }
}

package org.huangyalong.modules.notify.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.myframework.extra.dict.Dict;
import org.myframework.extra.dict.EnumDict;

import static org.myframework.extra.dict.IsDefault.NO;
import static org.myframework.extra.dict.IsDefault.YES;
import static org.myframework.extra.dict.Style.PRIMARY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Dict(name = "消息频率")
public enum NotifyFreq implements EnumDict<Integer> {
    TYPE0(1, "1分钟/次", 0, YES.getValue(), PRIMARY.getValue()),
    TYPE1(5, "5分钟/次", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE2(15, "15分钟/次", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE3(30, "30分钟/次", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE4(60, "1小时/次", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE5(60 * 5, "5小时/次", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE6(60 * 12, "12小时/次", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE7(60 * 24, "1天/次", 0, NO.getValue(), PRIMARY.getValue());

    @EnumValue
    @JsonValue
    private Integer value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;

    public static NotifyFreq fromValue(Integer value) {
        var enumClass = NotifyFreq.class;
        return EnumDict.fromValue(value, enumClass);
    }
}

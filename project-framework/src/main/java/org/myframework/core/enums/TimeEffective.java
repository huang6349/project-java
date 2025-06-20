package org.myframework.core.enums;

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
@Dict(name = "限制时间")
public enum TimeEffective implements EnumDict<String> {
    TYPE0("0", "不限制", 0, YES.getValue(), PRIMARY.getValue()),
    TYPE1("1", "限制", 0, NO.getValue(), PRIMARY.getValue());

    @EnumValue
    @JsonValue
    private String value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;
}

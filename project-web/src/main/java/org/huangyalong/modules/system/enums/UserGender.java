package org.huangyalong.modules.system.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.myframework.extra.dict.Dict;
import org.myframework.extra.dict.EnumDict;

import static org.myframework.extra.dict.IsDefault.NO;
import static org.myframework.extra.dict.Style.DEFAULT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Dict(name = "用户性别")
public enum UserGender implements EnumDict<String> {
    TYPE0("0", "男", 0, NO.getValue(), DEFAULT.getValue()),
    TYPE1("1", "女", 0, NO.getValue(), DEFAULT.getValue()),
    TYPE2("2", "其他", 1, NO.getValue(), DEFAULT.getValue());

    @EnumValue
    @JsonValue
    private String value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;
}

package org.huangyalong.modules.file.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.myframework.extra.dict.EnumDict;

import static org.myframework.extra.dict.IsDefault.NO;
import static org.myframework.extra.dict.IsDefault.YES;
import static org.myframework.extra.dict.Style.ERROR;
import static org.myframework.extra.dict.Style.PRIMARY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum FileStatus implements EnumDict<String> {
    TYPE0("0", "未使用", 0, YES.getValue(), PRIMARY.getValue()),
    TYPE1("1", "已使用", 0, NO.getValue(), ERROR.getValue());

    @EnumValue
    @JsonValue
    private String value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;
}

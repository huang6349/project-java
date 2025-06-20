package org.huangyalong.modules.system.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.myframework.extra.dict.Dict;
import org.myframework.extra.dict.EnumDict;

import static org.myframework.extra.dict.IsDefault.NO;
import static org.myframework.extra.dict.Style.PRIMARY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Dict(name = "租户类别")
public enum TenantCategory implements EnumDict<String> {
    TYPE0("0", "国有独资企业", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE1("1", "有限责任公司", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE2("2", "股份有限公司", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE3("3", "中外合资", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE4("4", "外商独资", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE5("5", "个人独资企业", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE6("6", "合伙企业", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE7("7", "中外合作经营企业", 0, NO.getValue(), PRIMARY.getValue()),
    TYPE8("8", "其它", 0, NO.getValue(), PRIMARY.getValue());

    @EnumValue
    @JsonValue
    private String value;

    private String label;

    private Integer sort;

    private Integer isDefault;

    private Integer style;
}

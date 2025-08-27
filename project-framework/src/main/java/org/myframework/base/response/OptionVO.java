package org.myframework.base.response;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static cn.hutool.core.text.CharSequenceUtil.format;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OptionVO implements Serializable {

    @Schema(description = "选项名称")
    private String label;

    @Schema(description = "选项键值")
    private String value;

    @JsonIgnore
    @Schema(description = "选项备注")
    private String desc;

    public String getLabel() {
        return ObjectUtil.isNotEmpty(desc)
                ? format("{}（{}）", label, desc)
                : label;
    }
}

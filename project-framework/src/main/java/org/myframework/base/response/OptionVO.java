package org.myframework.base.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OptionVO implements Serializable {

    @Schema(description = "选项名称")
    private String label;

    @Schema(description = "选项键值")
    private String value;
}

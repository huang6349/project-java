package org.huangyalong.modules.example.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "示例信息-BO")
public class ExampleBO extends BaseBO<Long> {

    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称的长度只能小于50个字符")
    @Schema(description = "示例名称", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "备注")
    private String desc;
}

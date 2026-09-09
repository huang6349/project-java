package org.huangyalong.modules.notify.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "消息应用-BO")
public class AppBO extends BaseBO<Long> {

    @NotNull(message = "类别不能为空")
    @Schema(description = "类别主键", requiredMode = REQUIRED)
    private Long categoryId;

    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称的长度只能小于50个字符")
    @Schema(description = "应用名称", requiredMode = REQUIRED)
    private String name;

    @NotNull(message = "频率不能为空")
    @Schema(description = "消息频率", requiredMode = REQUIRED)
    private Integer freq;

    @Schema(description = "备注")
    private String desc;
}

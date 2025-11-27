package org.huangyalong.modules.notify.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.huangyalong.core.constants.RegexpConstants;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "消息类别-BO")
public class CategoryBO extends BaseBO<Long> {

    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称的长度只能小于50个字符")
    @Schema(description = "类别名称", requiredMode = REQUIRED)
    private String name;

    @NotBlank(message = "代码不能为空")
    @Pattern(regexp = RegexpConstants.CODE, message = "错误的代码格式")
    @Size(max = 50, message = "代码的长度只能小于50个字符")
    @Schema(description = "类别代码", requiredMode = REQUIRED)
    private String code;

    @NotNull(message = "频率不能为空")
    @Schema(description = "消息频率", requiredMode = REQUIRED)
    private Integer freq;

    @Schema(description = "备注")
    private String desc;
}

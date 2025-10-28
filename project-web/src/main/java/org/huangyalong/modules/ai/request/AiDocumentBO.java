package org.huangyalong.modules.ai.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;
import org.myframework.base.validation.Save;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文档信息-BO")
public class AiDocumentBO extends BaseBO<Long> {

    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称的长度只能小于50个字符")
    @Schema(description = "文档名称", requiredMode = REQUIRED)
    private String name;

    @NotBlank(message = "文件不能为空", groups = Save.class)
    @Schema(description = "文件名称")
    private String filename;

    @Schema(description = "备注")
    private String desc;
}

package org.huangyalong.modules.file.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString
@Schema(name = "上传文件-BO")
public class UploadBO implements Serializable {

    @NotBlank(message = "文件不能为空")
    @Schema(description = "文件名称", requiredMode = REQUIRED)
    private String filename;
}

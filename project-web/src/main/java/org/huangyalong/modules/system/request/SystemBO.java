package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.huangyalong.core.constants.RegexpConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString
@Schema(name = "系统配置-BO")
public class SystemBO implements Serializable {

    @NotBlank(message = "代码不能为空")
    @Pattern(regexp = RegexpConstants.CODE, message = "错误的代码格式")
    @Size(max = 50, message = "代码的长度只能小于50个字符")
    @Schema(description = "配置代码", requiredMode = REQUIRED)
    private String code;

    @NotNull(message = "配置不能为空")
    @Schema(description = "配置信息", requiredMode = REQUIRED)
    private Map<String, Object> configs;
}

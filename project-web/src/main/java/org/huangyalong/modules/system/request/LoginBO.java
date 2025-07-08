package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString
@Schema(name = "登录信息-BO")
public class LoginBO implements Serializable {

    @NotBlank(message = "帐号不能为空")
    @Schema(description = "登录帐号", requiredMode = REQUIRED)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "登录密码", requiredMode = REQUIRED)
    private String password;
}

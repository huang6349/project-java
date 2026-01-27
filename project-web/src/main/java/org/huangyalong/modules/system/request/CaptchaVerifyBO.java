package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString
@Schema(name = "验证码校验-BO")
public class CaptchaVerifyBO implements Serializable {

    @NotBlank(message = "令牌不能为空")
    @Schema(description = "校验令牌", requiredMode = REQUIRED)
    private String verifyToken;
}

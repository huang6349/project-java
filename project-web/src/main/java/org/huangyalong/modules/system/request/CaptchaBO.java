package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Schema(name = "验证码-BO")
public class CaptchaBO implements Serializable {

    @Schema(description = "点坐标")
    private String pointJson;

    @Schema(description = "验证码标识")
    private String token;

    @Schema(description = "客户端标识")
    private String clientUid;

    @Schema(description = "请求时间")
    private Long ts;
}

package org.myframework.extra.notify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 消息推送请求
 */
@Data
@Schema(description = "消息推送")
public class NotifySend implements Serializable {

    @NotBlank(message = "代码不能为空")
    @Schema(description = "渠道代码", requiredMode = REQUIRED)
    private String code;

    @NotBlank(message = "来源应用不能为空")
    @Schema(description = "来源应用", requiredMode = REQUIRED)
    private String app;

    @NotBlank(message = "推送地址不能为空")
    @Schema(description = "推送地址", requiredMode = REQUIRED)
    private String recipient;

    @Schema(description = "渠道标识")
    private String id;

    @Schema(description = "附加参数")
    private Map<String, Object> extras;
}

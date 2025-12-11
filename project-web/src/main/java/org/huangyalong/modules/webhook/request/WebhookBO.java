package org.huangyalong.modules.webhook.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "网络钩子-BO")
public class WebhookBO extends BaseBO<Long> {

    @NotBlank(message = "推送地址不能为空")
    @Size(max = 512, message = "地址的长度只能小于512个字符")
    @Schema(description = "推送地址", requiredMode = REQUIRED)
    private String url;

    @NotBlank(message = "数据格式不能为空")
    @Schema(description = "数据格式")
    private String format;

    @Schema(description = "密钥文本")
    private String secret;

    @NotBlank(message = "触发事件不能为空")
    @Schema(description = "触发事件")
    private String trigger;

    @Size(max = 512, message = "备注的长度只能小于512个字符")
    @Schema(description = "备注")
    private String desc;
}

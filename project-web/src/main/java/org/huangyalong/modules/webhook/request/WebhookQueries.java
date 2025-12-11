package org.huangyalong.modules.webhook.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "网络钩子-Queries")
public class WebhookQueries extends BaseQueries {

    @Schema(description = "推送地址")
    private String url;

    @Schema(description = "数据格式")
    private String format;

    @Schema(description = "触发事件")
    private String trigger;

    @Schema(description = "备注")
    private String desc;
}

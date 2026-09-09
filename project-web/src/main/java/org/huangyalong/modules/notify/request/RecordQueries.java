package org.huangyalong.modules.notify.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "消息记录-Queries")
public class RecordQueries extends BaseQueries {

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "类别代码")
    private String code;

    @Schema(description = "来源应用")
    private String app;

    @Schema(description = "来源名称")
    private String appName;

    @Schema(description = "消息地址")
    private String recipient;

    @Schema(description = "消息内容")
    private String content;
}

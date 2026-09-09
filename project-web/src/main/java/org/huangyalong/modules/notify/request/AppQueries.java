package org.huangyalong.modules.notify.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "消息应用-Queries")
public class AppQueries extends BaseQueries {

    @Schema(description = "类别主键")
    private Long categoryId;

    @Schema(description = "应用名称")
    private String name;

    @Schema(description = "应用代码")
    private String code;
}

package org.huangyalong.modules.notify.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "消息类别-Queries")
public class CategoryQueries extends BaseQueries {

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "类别代码")
    private String code;
}

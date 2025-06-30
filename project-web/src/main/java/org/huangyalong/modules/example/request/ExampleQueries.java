package org.huangyalong.modules.example.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "示例信息-Queries")
public class ExampleQueries extends BaseQueries {

    @Schema(description = "示例名称")
    private String name;

    @Schema(description = "示例代码")
    private String code;
}

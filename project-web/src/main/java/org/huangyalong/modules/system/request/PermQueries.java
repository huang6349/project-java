package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "权限信息-Queries")
public class PermQueries extends BaseQueries {

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限代码")
    private String code;
}

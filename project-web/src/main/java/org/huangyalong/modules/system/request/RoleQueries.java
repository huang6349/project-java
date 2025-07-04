package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "角色查询-BO")
public class RoleQueries extends BaseQueries {

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色代码")
    private String code;
}

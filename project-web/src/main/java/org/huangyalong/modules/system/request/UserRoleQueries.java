package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户角色-Queries")
public class UserRoleQueries extends BaseQueries {

    @Schema(description = "数据主键", requiredMode = REQUIRED)
    private Long id;

    @Schema(description = "租户主键", requiredMode = REQUIRED)
    private Long tenantId;
}

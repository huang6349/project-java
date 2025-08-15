package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户权限-Queries")
public class UserPermQueries extends BaseQueries {

    @NotNull(message = "主键不能为空")
    @Schema(description = "数据主键", requiredMode = REQUIRED)
    private Long id;

    @NotNull(message = "租户不能为空")
    @Schema(description = "租户主键", requiredMode = REQUIRED)
    private Long tenantId;
}

package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "角色解除-BO")
public class RoleDissocBO extends BaseBO<Long> {

    @NotBlank(message = "表名不能为空")
    @Schema(description = "关联表名", requiredMode = REQUIRED)
    private String assoc;

    @NotNull(message = "租户不能为空")
    @Schema(description = "租户主键", requiredMode = REQUIRED)
    private Long tenantId;
}

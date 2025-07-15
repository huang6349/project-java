package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "角色关联-BO")
public class RoleAssocBO extends RoleDissocBO {

    @NotNull(message = "角色不能为空")
    @Schema(description = "角色主键", requiredMode = REQUIRED)
    private List<Long> roleIds;
}

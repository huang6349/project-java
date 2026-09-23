package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString
@Schema(name = "切换租户-BO")
public class TenantSwitchBO implements Serializable {

    @NotNull(message = "租户不能为空")
    @Schema(description = "租户主键", requiredMode = REQUIRED)
    private Long tenantId;
}

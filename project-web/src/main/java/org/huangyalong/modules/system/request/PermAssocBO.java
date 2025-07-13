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
@Schema(name = "权限关联-BO")
public class PermAssocBO extends PermDissocBO {

    @NotNull(message = "权限不能为空")
    @Schema(description = "权限主键", requiredMode = REQUIRED)
    private List<Long> permIds;
}

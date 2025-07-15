package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.NotNull;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户权限-BO")
public class UserPermBO extends BaseBO<Long> {

    @NotNull(message = "权限不能为空")
    @Schema(description = "权限主键", requiredMode = REQUIRED)
    private List<Long> permIds;
}

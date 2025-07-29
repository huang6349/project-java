package org.huangyalong.modules.system.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.response.BaseVO;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "租户用户")
public class TenantUserVO extends BaseVO<Long> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "租户主键")
    private Long tenantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "用户主键")
    private Long userId;

    @Schema(description = "备注")
    private String desc;
}

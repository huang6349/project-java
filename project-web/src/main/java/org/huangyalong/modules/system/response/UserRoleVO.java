package org.huangyalong.modules.system.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Schema(name = "用户角色-VO")
public class UserRoleVO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "数据主键")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "租户主键")
    private Long tenantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "角色主键")
    private List<Long> roleIds;
}

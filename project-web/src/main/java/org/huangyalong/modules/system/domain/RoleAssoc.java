package org.huangyalong.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.myframework.base.domain.SuperEntity;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Date;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_role_assoc")
@Schema(name = "角色关联")
public class RoleAssoc extends SuperEntity<RoleAssoc, Long> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "角色主键")
    private Long roleId;

    @Schema(description = "关联表名")
    private String assoc;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "关联主键")
    private Long assocId;

    @JKDictFormat
    @Schema(description = "限制时间")
    private TimeEffective effective;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "有效时间")
    private Date effectiveTime;

    @JKDictFormat
    @Schema(description = "关联类别")
    private AssocCategory category;

    @Schema(description = "备注")
    private String desc;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "租户主键")
    private Long tenantId;
}

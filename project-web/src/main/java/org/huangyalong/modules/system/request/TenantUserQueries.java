package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "租户用户-Queries")
public class TenantUserQueries extends BaseQueries {

    @Schema(description = "租户主键")
    private Long tenantId;

    @Schema(description = "用户帐号")
    private String username;

    @Schema(description = "手机号码")
    private String mobile;
}

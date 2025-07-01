package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "租户信息-Queries")
public class TenantQueries extends BaseQueries {

    @Schema(description = "租户名称")
    private String name;

    @Schema(description = "租户代码")
    private String code;
}

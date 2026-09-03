package org.myframework.qdb.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分页查询-QdbQueries")
public class QdbPageQueries extends BaseQueries {

    @Schema(description = "当前页游标")
    private String searchAfter;

    @Schema(description = "每页数量", requiredMode = REQUIRED)
    private Integer pageSize;
}

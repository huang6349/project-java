package org.myframework.base.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分页信息-Queries")
public class PageQueries extends BaseQueries {

    public static Long DEFAULT_PAGE_NUMBER = 1L;

    public static Long DEFAULT_PAGE_SIZE = 10L;

    @Schema(description = "当前页码", requiredMode = REQUIRED)
    private Long pageNumber;

    @Schema(description = "每页数量", requiredMode = REQUIRED)
    private Long pageSize;
}

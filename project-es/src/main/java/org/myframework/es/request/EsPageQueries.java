package org.myframework.es.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;
import org.myframework.es.service.EsService;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分页查询-EsQueries")
public class EsPageQueries extends BaseQueries {

    public static Integer DEFAULT_PAGE_NUMBER = EsService.DEFAULT_PAGE_NUMBER;

    public static Integer DEFAULT_PAGE_SIZE = EsService.DEFAULT_PAGE_SIZE;

    @Schema(description = "当前页码", requiredMode = REQUIRED)
    private Integer pageNumber;

    @Schema(description = "每页数量", requiredMode = REQUIRED)
    private Integer pageSize;
}

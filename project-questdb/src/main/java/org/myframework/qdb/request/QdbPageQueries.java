package org.myframework.qdb.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;
import org.myframework.qdb.service.QdbService;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分页查询-QdbQueries")
public class QdbPageQueries extends BaseQueries {

    public static Integer DEFAULT_PAGE_SIZE = QdbService.DEFAULT_PAGE_SIZE;

    @Schema(description = "每页数量", requiredMode = REQUIRED)
    private Integer pageSize;

    @Schema(description = "分页游标")
    private String cursor;
}

package org.myframework.qdb.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QdbPageVO<T> implements Serializable {

    @Schema(description = "下一页游标")
    private String nextSearchAfter;

    @Schema(description = "当前页游标")
    private String searchAfter;

    @Schema(description = "当前页数据")
    private List<T> list;

    @Schema(description = "每页数量")
    private Integer pageSize;
}

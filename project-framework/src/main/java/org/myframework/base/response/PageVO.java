package org.myframework.base.response;

import cn.hutool.core.lang.Opt;
import com.mybatisflex.core.paginate.Page;
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
public class PageVO<T> implements Serializable {

    @Schema(description = "当前页数据")
    private List<T> list;

    @Schema(description = "总页数")
    private Long total;

    public static <T> PageVO<T> of(Page<T> page) {
        var totalRow = Opt.of(page)
                .map(Page::getTotalRow)
                .get();
        var records = Opt.of(page)
                .map(Page::getRecords)
                .get();
        return new PageVO<T>()
                .setTotal(totalRow)
                .setList(records);
    }
}

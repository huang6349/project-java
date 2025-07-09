package org.myframework.es.response;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.easyes.core.biz.EsPageInfo;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EsPageVO<T> implements Serializable {

    @Schema(description = "当前页数据")
    private List<T> list;

    @Schema(description = "总页数")
    private Long total;

    public static <T> EsPageVO<T> of(EsPageInfo<T> pageInfo) {
        var total = Opt.ofNullable(pageInfo)
                .map(EsPageInfo::getTotal)
                .get();
        var list = Opt.ofNullable(pageInfo)
                .map(EsPageInfo::getList)
                .get();
        return new EsPageVO<T>()
                .setTotal(total)
                .setList(list);
    }
}

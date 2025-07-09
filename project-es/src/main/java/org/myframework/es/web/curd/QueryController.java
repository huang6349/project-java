package org.myframework.es.web.curd;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.kernel.Wrapper;
import org.myframework.base.response.ApiResponse;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.myframework.es.request.EsPageQueries;
import org.myframework.es.response.EsPageVO;
import org.myframework.es.web.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.myframework.es.request.EsPageQueries.DEFAULT_PAGE_NUMBER;
import static org.myframework.es.request.EsPageQueries.DEFAULT_PAGE_SIZE;

public interface QueryController<Entity, Id extends Serializable, Queries> extends BaseController<Entity> {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query/paging")
    @Operation(summary = "分页查询")
    default Mono<EsPageVO<Entity>> queryPage(EsPageQueries pageQueries,
                                             Queries queries) {
        var result = handlerQuery(queries);
        var query = result.getData();
        var pageNumber = Opt.ofNullable(pageQueries)
                .map(EsPageQueries::getPageNumber)
                .orElse(DEFAULT_PAGE_NUMBER);
        var pageSize = Opt.ofNullable(pageQueries)
                .map(EsPageQueries::getPageSize)
                .orElse(DEFAULT_PAGE_SIZE);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getBaseService()
                    .page(pageNumber, pageSize, query)
                    .map(EsPageVO::of);
        return getBaseService()
                .page(pageNumber, pageSize)
                .map(EsPageVO::of);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query")
    @Operation(summary = "批量查询")
    default Flux<Entity> query(Queries queries) {
        var result = handlerQuery(queries);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getBaseService()
                    .list(query);
        return getBaseService()
                .list(null);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}")
    @Operation(summary = "单体查询")
    default Mono<Entity> getById(@PathVariable Id id) {
        var result = handlerQuery(id);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getBaseService()
                    .getOne(query);
        return getBaseService()
                .getById(id);
    }

    default ApiResponse<Wrapper<Entity>> handlerQuery(Queries queries) {
        StaticLog.trace("构造查询条件: {}", queries);
        if (ObjectUtil.isNull(queries))
            return ApiResponse.okDef();
        // Queries -> Map
        var map = BeanUtil.beanToMap(queries);
        var query = new LambdaEsQueryWrapper<Entity>()
                .allEq(map);
        return ApiResponse.ok(query);
    }

    default ApiResponse<Wrapper<Entity>> handlerQuery(Id id) {
        StaticLog.trace("构造查询条件: {}", id);
        return ApiResponse.okDef();
    }
}

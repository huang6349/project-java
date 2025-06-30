package org.myframework.base.web.reactive;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.request.PageQueries;
import org.myframework.base.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.myframework.base.request.PageQueries.DEFAULT_PAGE_NUMBER;
import static org.myframework.base.request.PageQueries.DEFAULT_PAGE_SIZE;

public interface ReactiveQueryController<Entity, Id extends Serializable, Queries> extends ReactiveBaseController<Entity> {

    @GetMapping("/_query/paging")
    @Operation(summary = "分页查询")
    default Mono<Page<Entity>> queryPage(PageQueries pageQueries,
                                         Queries queries) {
        var result = handlerQuery(queries);
        var query = result.getData();
        var pageNumber = Opt.ofNullable(pageQueries)
                .map(PageQueries::getPageNumber)
                .orElse(DEFAULT_PAGE_NUMBER);
        var pageSize = Opt.ofNullable(pageQueries)
                .map(PageQueries::getPageSize)
                .orElse(DEFAULT_PAGE_SIZE);
        var page = new Page<Entity>(pageNumber, pageSize);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getReactorService()
                    .page(page, query);
        return getReactorService()
                .page(page);
    }

    @GetMapping("/_query")
    @Operation(summary = "批量查询")
    default Flux<Entity> query(Queries queries) {
        var result = handlerQuery(queries);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getReactorService()
                    .list(query);
        return getReactorService()
                .list();
    }

    @GetMapping("/{id:.+}")
    @Operation(summary = "单体查询")
    default Mono<Entity> getById(@PathVariable Id id) {
        var result = handlerQuery(id);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getReactorService()
                    .getOne(query);
        return getReactorService()
                .getById(id);
    }

    default ApiResponse<QueryWrapper> handlerQuery(Queries queries) {
        StaticLog.trace("构造查询条件: {}", queries);
        if (ObjectUtil.isNull(queries))
            return ApiResponse.okDef();
        // Queries -> Entity
        var entity = BeanUtil.toBean(queries, getEntityClass());
        var query = QueryWrapper.create(entity);
        return ApiResponse.ok(query);
    }

    default ApiResponse<QueryWrapper> handlerQuery(Id id) {
        StaticLog.trace("构造查询条件: {}", id);
        return ApiResponse.okDef();
    }
}

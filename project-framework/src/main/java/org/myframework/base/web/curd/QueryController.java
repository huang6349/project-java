package org.myframework.base.web.curd;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.request.PageQueries;
import org.myframework.base.response.PageVO;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.BaseController;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.myframework.base.request.PageQueries.DEFAULT_PAGE_NUMBER;
import static org.myframework.base.request.PageQueries.DEFAULT_PAGE_SIZE;

public interface QueryController<Entity, Id extends Serializable, Queries> extends BaseController<Entity> {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query/paging")
    @Operation(summary = "根据条件查询(分页查询)")
    default Mono<PageVO<Entity>> queryPage(PageQueries pageQueries,
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
            return getBaseService()
                    .pageOnce(page, query)
                    .map(PageVO::of);
        return getBaseService()
                .pageOnce(page)
                .map(PageVO::of);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query")
    @Operation(summary = "根据条件查询(列表查询)")
    default Flux<Entity> query(Queries queries) {
        var result = handlerQuery(queries);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getBaseService()
                    .list(query);
        return getBaseService()
                .list();
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}")
    @Operation(summary = "根据主键查询")
    default Mono<Entity> getById(@PathVariable Id id) {
        var result = handlerQuery(id);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getBaseService()
                    .getOne(query);
        return getBaseService()
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

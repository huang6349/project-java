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
import java.util.List;

import static org.myframework.base.request.PageQueries.DEFAULT_PAGE_NUMBER;
import static org.myframework.base.request.PageQueries.DEFAULT_PAGE_SIZE;

public interface QueryController<Entity, Id extends Serializable, Queries>
        extends BaseController<Entity>, QueryAfterHandler<Entity> {

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

        Mono<PageVO<Entity>> pageMono;
        if (BooleanUtil.isFalse(result.getDefExec()))
            pageMono = getBaseService()
                    .pageOnce(page, query)
                    .map(PageVO::of);
        else
            pageMono = getBaseService()
                    .pageOnce(page)
                    .map(PageVO::of);

        // 后置处理
        return handlerAfterPage(pageMono);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query")
    @Operation(summary = "根据条件查询(列表查询)")
    default Flux<Entity> query(Queries queries) {
        var result = handlerQuery(queries);
        var query = result.getData();

        Flux<Entity> entities;
        if (BooleanUtil.isFalse(result.getDefExec()))
            entities = getBaseService().list(query);
        else
            entities = getBaseService().list();

        // 后置处理：收集 → handlerAfterList → 展开
        return Flux.defer(() -> {
            Mono<List<Entity>> listMono = entities.collectList();
            Mono<List<Entity>> processedMono = handlerAfterList(listMono);
            return processedMono.flatMapMany(Flux::fromIterable);
        });
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}")
    @Operation(summary = "根据主键查询")
    default Mono<Entity> getById(@PathVariable Id id) {
        var result = handlerQuery(id);
        var query = result.getData();

        Mono<Entity> entityMono;
        if (BooleanUtil.isFalse(result.getDefExec()))
            entityMono = getBaseService().getOne(query);
        else
            entityMono = getBaseService().getById(id);

        // 后置处理
        return entityMono.flatMap(this::handlerAfter);
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

package org.myframework.qdb.web.curd;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.row.DbChain;
import com.mybatisflex.core.row.Row;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.myframework.qdb.request.QdbPageQueries;
import org.myframework.qdb.response.QdbPageVO;
import org.myframework.qdb.web.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.bean.BeanUtil.beanToMap;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.myframework.qdb.helper.QdbHelper.*;

/**
 * QuestDB 查询控制器（参考 {@link org.myframework.base.web.curd.QueryController}）
 * <p>
 * 差异说明：
 * <ul>
 *     <li>无强类型实体：{@link #handlerQuery(Queries)} 通过 {@link DbChain#where(java.util.Map)} 构造等值条件</li>
 *     <li>{@code id} 视为普通列等值条件（questdb 无主键，无 Service 级 getById）</li>
 *     <li>分页用键集分页（{@code /_query/after}，QuestDB 不支持 OFFSET）</li>
 *     <li>defExec 已废弃：handlerQuery 总是返回带表名 wrapper，controller 端点统一单分支调用</li>
 * </ul>
 */
public interface QueryController<Id extends Serializable, Queries> extends BaseController {

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query/after")
    @Operation(summary = "键集分页")
    default Mono<QdbPageVO<Row>> queryAfter(QdbPageQueries pageQueries,
                                            Queries queries) {
        return Mono.fromCallable(() -> {
            var result = handlerQuery(queries);
            var query = result.getData();
            var searchAfter = Opt.ofNullable(pageQueries)
                    .map(QdbPageQueries::getSearchAfter)
                    .get();
            var pageSize = Opt.ofNullable(pageQueries)
                    .map(QdbPageQueries::getPageSize)
                    .orElse(DEFAULT_PAGE_SIZE);
            return listAfter(searchAfter, pageSize, query);
        });
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query")
    @Operation(summary = "批量查询")
    default Flux<Row> query(Queries queries) {
        return Mono.fromCallable(() -> {
            var result = handlerQuery(queries);
            var query = result.getData();
            return list(query);
        }).flatMapMany(Flux::fromIterable);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/_query/last")
    @Operation(summary = "瞬时查询")
    default Mono<Row> last(Queries queries) {
        return Mono.fromCallable(() -> {
            var result = handlerQuery(queries);
            var query = result.getData();
            return getOne(query);
        });
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{id:.+}")
    @Operation(summary = "单体查询")
    default Mono<Row> getById(@PathVariable Id id) {
        return Mono.fromCallable(() -> {
            var result = handlerQuery(id);
            var query = result.getData();
            return getOne(query);
        });
    }

    /**
     * 构造查询条件
     * <p>
     * Queries -&gt; Map（忽略 null 字段）-&gt; 等值条件，表名来自 {@link #getTableName()}
     */
    default ApiResponse<DbChain> handlerQuery(Queries queries) {
        StaticLog.trace("构造查询条件: {}", queries);
        var query = DbChain.table(getTableName());
        if (ObjectUtil.isNull(queries))
            return ApiResponse.ok(query);
        query.where(beanToMap(queries, FALSE, TRUE));
        return ApiResponse.ok(query);
    }

    /**
     * 构造主键查询条件（questdb 无主键，id 视为普通列等值条件）
     */
    default ApiResponse<DbChain> handlerQuery(Id id) {
        StaticLog.trace("构造查询条件: {}", id);
        var query = DbChain.table(getTableName());
        query.eq(ID_KEY, id);
        return ApiResponse.ok(query);
    }
}

package org.myframework.es.service;

import cn.hutool.core.lang.Opt;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.util.SqlUtil;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.kernel.BaseEsMapper;
import org.dromara.easyes.core.kernel.Wrapper;
import org.myframework.core.util.ReactorUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@SuppressWarnings({"CodeBlock2Expr", "unused"})
public interface EsService<Entity> {

    int DEFAULT_PAGE_NUMBER = 1;

    int DEFAULT_PAGE_SIZE = 10;

    int MAX_TOTAL = 10000;

    BaseEsMapper<Entity> getMapper();

    // ===== 保存（增）操作 =====

    default Mono<Boolean> save(Entity entity) {
        StaticLog.trace("保存实体类对象数据");
        return Mono.fromSupplier(() -> {
            var result = getMapper()
                    .insert(entity);
            return SqlUtil.toBool(result);
        });
    }

    // ===== 保存（增）操作 =====

    default Mono<Entity> getById(Serializable id) {
        StaticLog.trace("根据数据主键查询一条数据");
        return Mono.fromSupplier(() -> {
            return getMapper()
                    .selectById(id);
        });
    }

    default Mono<Entity> getOne(Wrapper<Entity> query) {
        StaticLog.trace("根据查询条件查询一条数据");
        return Mono.fromSupplier(() -> {
            return getMapper()
                    .selectOne(query);
        });
    }

    default Flux<Entity> list(Wrapper<Entity> query) {
        StaticLog.trace("根据查询条件查询数据集合");
        return ReactorUtil.toFlux(() -> {
            return getMapper()
                    .selectList(query);
        });
    }

    default Flux<Entity> list() {
        StaticLog.trace("根据查询条件查询数据集合");
        return list(query());
    }

    default Mono<Long> count(Wrapper<Entity> query) {
        StaticLog.trace("根据查询条件查询数据数量");
        return Mono.fromSupplier(() -> {
            return getMapper()
                    .selectCount(query);
        });
    }

    default Mono<Long> count() {
        StaticLog.trace("查询所有数据数量");
        return count(query());
    }

    // ===== 分页查询操作 =====

    default Mono<EsPageInfo<Entity>> page(Integer pageNumber,
                                          Integer pageSize,
                                          Wrapper<Entity> query) {
        StaticLog.trace("根据查询条件分页查询数据");
        return Mono.fromSupplier(() -> {
            var number = Opt.ofNullable(pageNumber)
                    .orElse(DEFAULT_PAGE_NUMBER);
            var size = Opt.ofNullable(pageSize)
                    .orElse(DEFAULT_PAGE_SIZE);
            var offset = (number - 1) * size;
            if (offset + size > MAX_TOTAL)
                throw new RuntimeException("分页查询不能超过" + MAX_TOTAL + "条记录");
            return getMapper()
                    .pageQuery(query, number, size);
        });
    }

    default Mono<EsPageInfo<Entity>> page(Integer pageNumber,
                                          Integer pageSize) {
        StaticLog.trace("根据查询条件分页查询数据");
        return page(pageNumber,
                pageSize,
                query()
        );
    }

    // ===== 查询包装器操作 =====

    default LambdaEsQueryWrapper<Entity> query() {
        StaticLog.trace("默认查询包装器构建");
        return new LambdaEsQueryWrapper<>();
    }
}

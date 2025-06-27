package org.myframework.base.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.update.UpdateChain;
import org.myframework.core.util.ReactorUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings({"CodeBlock2Expr", "unused"})
public interface ReactorService<Entity> {

    BaseMapper<Entity> getMapper();

    IService<Entity> getBlockService();

    // ===== 保存（增）操作 =====

    default Mono<Boolean> save(Entity entity) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .save(entity);
        });
    }

    default Mono<Boolean> saveBatch(Collection<Entity> entities) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .saveBatch(entities);
        });
    }

    default Mono<Boolean> saveBatch(Collection<Entity> entities,
                                    int batchSize) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .saveBatch(entities, batchSize);
        });
    }

    default Mono<Boolean> saveOrUpdate(Entity entity) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .saveOrUpdate(entity);
        });
    }

    default Mono<Boolean> saveOrUpdateBatch(Collection<Entity> entities) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .saveOrUpdateBatch(entities);
        });
    }

    default Mono<Boolean> saveOrUpdateBatch(Collection<Entity> entities,
                                            int batchSize) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .saveOrUpdateBatch(entities, batchSize);
        });
    }

    // ===== 删除（删）操作 =====

    default Mono<Boolean> remove(QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .remove(query);
        });
    }

    default Mono<Boolean> remove(QueryCondition condition) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .remove(condition);
        });
    }

    default Mono<Boolean> removeById(Entity entity) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .removeById(entity);
        });
    }

    default Mono<Boolean> removeById(Serializable id) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .removeById(id);
        });
    }

    default Mono<Boolean> removeByIds(Collection<? extends Serializable> ids) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .removeByIds(ids);
        });
    }

    default Mono<Boolean> removeByMap(Map<String, Object> query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .removeByMap(query);
        });
    }

    // ===== 更新（改）操作 =====

    default Mono<Boolean> updateById(Entity entity) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .updateById(entity);
        });
    }

    default Mono<Boolean> updateById(Entity entity,
                                     boolean ignoreNulls) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .updateById(entity, ignoreNulls);
        });
    }

    default Mono<Boolean> update(Entity entity,
                                 Map<String, Object> query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .update(entity, query);
        });
    }

    default Mono<Boolean> update(Entity entity,
                                 QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .update(entity, query);
        });
    }

    default Mono<Boolean> update(Entity entity,
                                 QueryCondition condition) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .update(entity, condition);
        });
    }

    default Mono<Boolean> updateBatch(Collection<Entity> entities) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .updateBatch(entities);
        });
    }

    default Mono<Boolean> updateBatch(Collection<Entity> entities,
                                      boolean ignoreNulls) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .updateBatch(entities, ignoreNulls);
        });
    }

    default Mono<Boolean> updateBatch(Collection<Entity> entities,
                                      int batchSize) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .updateBatch(entities, batchSize);
        });
    }

    default Mono<Boolean> updateBatch(Collection<Entity> entities,
                                      int batchSize,
                                      boolean ignoreNulls) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .updateBatch(entities, batchSize, ignoreNulls);
        });
    }

    // ===== 查询（查）操作 =====

    default Mono<Entity> getById(Serializable id) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getById(id);
        });
    }

    default Mono<Entity> getOneByEntityId(Entity entity) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getOneByEntityId(entity);
        });
    }

    default Mono<Entity> getOne(QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getOne(query);
        });
    }

    default <R> Mono<R> getOneAs(QueryWrapper query,
                                 Class<R> asType) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getOneAs(query, asType);
        });
    }

    default Mono<Entity> getOne(QueryCondition condition) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getOne(condition);
        });
    }

    default Mono<Object> getObj(QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getObj(query);
        });
    }

    default <R> Mono<R> getObjAs(QueryWrapper query,
                                 Class<R> asType) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .getObjAs(query, asType);
        });
    }

    default Flux<Object> objList(QueryWrapper query) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .objList(query);
        });
    }

    default <R> Flux<R> objListAs(QueryWrapper query,
                                  Class<R> asType) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .objListAs(query, asType);
        });
    }

    default Flux<Entity> list() {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .list();
        });
    }

    default Flux<Entity> list(QueryWrapper query) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .list(query);
        });
    }

    default Flux<Entity> list(QueryCondition condition) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .list(condition);
        });
    }

    default <R> Flux<R> listAs(QueryWrapper query,
                               Class<R> asType) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .listAs(query, asType);
        });
    }

    default Flux<Entity> listByIds(Collection<? extends Serializable> ids) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .listByIds(ids);
        });
    }

    default Flux<Entity> listByMap(Map<String, Object> query) {
        return ReactorUtil.toFlux(() -> {
            return getBlockService()
                    .listByMap(query);
        });
    }

    // ===== 数量查询操作 =====

    default Mono<Boolean> exists(QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .exists(query);
        });
    }

    default Mono<Boolean> exists(QueryCondition condition) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .exists(condition);
        });
    }

    default Mono<Long> count() {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .count();
        });
    }

    default Mono<Long> count(QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .count(query);
        });
    }

    default Mono<Long> count(QueryCondition condition) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .count(condition);
        });
    }

    // ===== 分页查询操作 =====

    default Mono<Page<Entity>> page(Page<Entity> page) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .page(page);
        });
    }

    default Mono<Page<Entity>> page(Page<Entity> page,
                                    QueryWrapper query) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .page(page, query);
        });
    }

    default Mono<Page<Entity>> page(Page<Entity> page,
                                    QueryCondition condition) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .page(page, condition);
        });
    }

    default <R> Mono<Page<R>> pageAs(Page<R> page,
                                     QueryWrapper query,
                                     Class<R> asType) {
        return Mono.fromSupplier(() -> {
            return getBlockService()
                    .pageAs(page, query, asType);
        });
    }

    // ===== 查询包装器操作 =====

    default UpdateChain<Entity> updateChain() {
        return getBlockService()
                .updateChain();
    }

    default QueryChain<Entity> queryChain() {
        return getBlockService()
                .queryChain();
    }

    default QueryWrapper query() {
        return getBlockService()
                .query();
    }
}

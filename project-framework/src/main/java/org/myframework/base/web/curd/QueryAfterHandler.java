package org.myframework.base.web.curd;

import com.mybatisflex.core.paginate.Page;
import org.myframework.base.response.PageVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 查询后置处理器，支持在查询完成后对数据进行二次处理。
 *
 * <ul>
 *     <li>关联数据补全（如查询用户时补全角色名称）</li>
 *     <li>字段格式化（如日期、数字本地化）</li>
 *     <li>权限过滤（数据级别）</li>
 *     <li>统计聚合（如追加合计行）</li>
 * </ul>
 *
 * @param <Entity> 实体类型
 */
public interface QueryAfterHandler<Entity> {

    /**
     * 单体查询后置处理。
     * <p>
     * 默认实现直接透传，覆写后可对单个实体进行二次处理。
     *
     * @param entity 查询结果（可能为 null）
     * @return 处理后的实体
     */
    default Mono<Entity> handlerAfter(Entity entity) {
        return Mono.justOrEmpty(entity);
    }

    /**
     * 列表后置处理（被 {@code query} 和 {@code queryPage} 共用）。
     * <p>
     * 默认实现直接透传，覆写后可对整页列表进行批量处理：
     * <ul>
     *     <li>批量关联补全（先收集 ID，再批量查，最后 match 回原列表）</li>
     *     <li>数据过滤或转换</li>
     *     <li>追加统计行/合计行</li>
     * </ul>
     *
     * @param entitiesMono 原始列表 Mono
     * @return 处理后的列表 Mono
     */
    default Mono<List<Entity>> handlerAfterList(Mono<List<Entity>> entitiesMono) {
        return entitiesMono;
    }

    /**
     * 分页查询后置处理。
     * <p>
     * 默认实现：提取 {@link PageVO} 内的 {@code list} →
     * {@link #handlerAfterList(Mono)} 处理 → 塞回 {@link PageVO}。
     * <p>
     * 覆写此方法可直接操作 {@link PageVO} 本身（如追加合计行、设置分页元数据）。
     *
     * @param pageMono 原始分页结果的 Mono
     * @return 处理后的分页结果 Mono
     */
    default Mono<PageVO<Entity>> handlerAfterPage(Mono<PageVO<Entity>> pageMono) {
        return pageMono.flatMap(page -> {
            Mono<List<Entity>> listMono = Mono.just(page.getList());
            return handlerAfterList(listMono)
                    .map(processed -> {
                        page.setList(processed);
                        return page;
                    });
        });
    }
}

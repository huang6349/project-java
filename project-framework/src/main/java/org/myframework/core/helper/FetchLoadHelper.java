package org.myframework.core.helper;

import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.text.CharSequenceUtil.*;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * 抽象缓存助手类
 * <p>
 * 提供 fetch 和 load 方法的抽象定义，使用 caffeine 实现本地缓存
 * 每个实现类拥有独立的缓存实例，支持自定义缓存配置
 * </p>
 *
 * @param <T> 缓存数据类型
 * @author huangyalong
 */
@SuppressWarnings("unused")
public abstract class FetchLoadHelper<T> {

    /**
     * 默认缓存过期时间：5分钟
     */
    protected static final long DEFAULT_EXPIRE_MINUTES = 5;

    /**
     * 获取缓存键前缀
     * <p>
     * 子类可覆盖以自定义缓存键前缀。
     * </p>
     *
     * @return 缓存键前缀
     */
    protected String getCacheKeyPrefix() {
        // 去掉 Helper 后缀，驼峰转短横线格式
        var simpleName = getClass().getSimpleName();
        var name = removeSuffix(simpleName, "Helper");
        return toSymbolCase(name, '-');
    }

    /**
     * 获取缓存实例
     * <p>
     * 子类可覆盖以自定义缓存配置。
     * </p>
     *
     * @return Caffeine Cache 实例
     */
    protected Cache<String, T> getCache() {
        // 双重检查锁：首次访问时延迟初始化，保证线程安全
        if (ObjectUtil.isNull(cache)) {
            synchronized (this) {
                if (ObjectUtil.isNull(cache)) {
                    // 获取子类定义的过期策略并构建缓存
                    var expireMinutes = getExpireMinutes();
                    var expireUnit = getExpireUnit();
                    cache = Caffeine.newBuilder()
                            .expireAfterWrite(expireMinutes, expireUnit)
                            .build();
                }
            }
        }
        return cache;
    }

    // 缓存实例，使用 volatile 保证可见性
    private volatile Cache<String, T> cache;

    /**
     * 获取缓存过期时间
     * <p>
     * 子类可覆盖以自定义过期时间。
     * </p>
     *
     * @return 过期时间值
     */
    protected long getExpireMinutes() {
        // 返回默认过期时间
        return DEFAULT_EXPIRE_MINUTES;
    }

    /**
     * 获取过期时间单位
     * <p>
     * 子类可覆盖以自定义过期单位。
     * </p>
     *
     * @return 时间单位
     */
    protected TimeUnit getExpireUnit() {
        // 返回默认时间单位：分钟
        return MINUTES;
    }

    /**
     * 从数据源获取数据
     * <p>
     * 子类必须实现此方法定义数据加载逻辑（如从数据库查询）。
     * </p>
     *
     * @param id 查询参数（通常为数据编号）
     * @return 数据结果，未找到返回 null
     */
    protected abstract T fetch(Serializable id);

    /**
     * 根据缓存键获取数据
     * <p>
     * 内部方法：解析缓存键，去掉 prefix- 前缀后调用子类的 {@link #fetch(Serializable)}。
     * </p>
     *
     * @param cacheKey 缓存键
     * @return 数据结果
     */
    private T fetch(String cacheKey) {
        var prefix = format("{}-", getCacheKeyPrefix());
        var id = removePrefix(cacheKey, prefix);
        // 强转为 Serializable，确保调用子类的 fetch(Serializable) 而非自身
        return fetch((Serializable) id);
    }

    /**
     * 构建缓存键
     *
     * @param id 查询参数
     * @return 缓存键，格式：prefix-id
     */
    protected String buildCacheKey(Serializable id) {
        var prefix = getCacheKeyPrefix();
        return format("{}-{}", prefix, id);
    }

    /**
     * 加载数据到缓存
     * <p>
     * 主动将数据加载到缓存中，适用于缓存预热或刷新场景。
     * </p>
     *
     * @param id 查询参数
     */
    public void load(Serializable id) {
        // 非空校验：id 为空时跳过
        if (ObjectUtil.isNotNull(id)) {
            var cacheKey = buildCacheKey(id);
            var data = fetch(id);
            // 只有数据非空时才写入缓存
            if (ObjectUtil.isNotNull(data)) {
                getCache().put(cacheKey, data);
            }
        }
    }

    /**
     * 获取缓存数据
     * <p>
     * 先从缓存获取，缓存未命中时自动调用 {@link #fetch(Serializable)} 回源加载。
     * </p>
     *
     * @param id 查询参数
     * @return 缓存数据或回源数据，id 为空返回 null
     */
    public T get(Serializable id) {
        // 空值检查：id 为空直接返回，避免无意义查询
        if (ObjectUtil.isNull(id))
            return null;
        var cacheKey = buildCacheKey(id);
        // Caffeine 的 get 方法在缓存未命中时自动调用 fetch 回源
        return getCache().get(cacheKey, this::fetch);
    }

    /**
     * 删除指定缓存
     *
     * @param id 查询参数
     */
    public void delete(Serializable id) {
        // 非空校验：id 为空时跳过
        if (ObjectUtil.isNotNull(id)) {
            var cacheKey = buildCacheKey(id);
            getCache().invalidate(cacheKey);
        }
    }

    /**
     * 清除所有缓存
     */
    public void clear() {
        // 清除当前 Helper 实例的所有缓存条目
        getCache().invalidateAll();
    }
}

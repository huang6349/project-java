package org.myframework.core.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.lang.Opt.ofNullable;
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
     * 默认缓存过期时间：30分钟
     */
    protected static final long DEFAULT_EXPIRE_MINUTES = 30;

    /**
     * 获取缓存键前缀
     * 子类可覆盖以自定义缓存键
     *
     * @return 缓存键前缀
     */
    protected String getCacheKeyPrefix() {
        // 子类覆盖此方法以自定义缓存键前缀
        return getClass().getSimpleName();
    }

    /**
     * 获取缓存实例
     * 子类可覆盖以自定义缓存配置
     *
     * @return Caffeine Cache 实例
     */
    protected Cache<Dict, T> getCache() {
        // 双重检查锁实现延迟初始化
        if (ObjectUtil.isNull(cache)) {
            synchronized (this) {
                if (ObjectUtil.isNull(cache)) {
                    // 获取过期配置并构建缓存
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
    private volatile Cache<Dict, T> cache;

    /**
     * 获取缓存过期时间
     * 子类可覆盖以自定义过期时间
     *
     * @return 过期时间值
     */
    protected long getExpireMinutes() {
        // 子类覆盖此方法以自定义过期时间
        return DEFAULT_EXPIRE_MINUTES;
    }

    /**
     * 获取过期时间单位
     * 子类可覆盖以自定义过期单位
     *
     * @return 时间单位
     */
    protected TimeUnit getExpireUnit() {
        // 子类覆盖此方法以自定义过期单位
        return MINUTES;
    }

    /**
     * 从数据源获取数据
     * 子类必须实现此方法定义数据加载逻辑
     *
     * @param dict 查询参数
     * @return 数据结果
     */
    protected abstract T fetch(Dict dict);

    /**
     * 加载数据到缓存
     *
     * @param dict 查询参数
     */
    public void load(Dict dict) {
        // 非空检查
        if (ObjectUtil.isNotEmpty(dict)) {
            // 构建缓存键
            var cacheKey = buildCacheKey(dict);
            // 从数据源获取数据
            var data = fetch(dict);
            // 写入缓存
            if (ObjectUtil.isNotEmpty(data)) {
                getCache().put(cacheKey, data);
            }
        }
    }

    /**
     * 获取缓存数据
     *
     * @param dict 查询参数
     * @return 缓存数据或加载的数据
     */
    public T get(Dict dict) {
        // 空值检查
        if (ObjectUtil.isEmpty(dict))
            return null;
        // 构建缓存键
        var cacheKey = buildCacheKey(dict);
        // Caffeine 的 get 方法会自动调用 fetch 回源
        return getCache().get(cacheKey, this::fetch);
    }

    /**
     * 构建缓存键
     *
     * @param dict 查询参数
     * @return 缓存键
     */
    protected Dict buildCacheKey(Dict dict) {
        // 缓存键由前缀和参数组成
        return Dict.create()
                .set("prefix", getCacheKeyPrefix())
                .set("params", dict);
    }

    /**
     * 获取缓存中的列表数据
     *
     * @param dict 查询参数
     * @return 缓存的数据列表
     */
    public List<T> getList(Dict dict) {
        // 强制转换为 List 类型
        return (List<T>) get(dict);
    }

    /**
     * 删除指定参数的缓存
     *
     * @param dict 查询参数
     */
    public void delete(Dict dict) {
        // 非空检查
        if (ObjectUtil.isNotEmpty(dict)) {
            // 构建缓存键并移除
            var cacheKey = buildCacheKey(dict);
            getCache().invalidate(cacheKey);
        }
    }

    /**
     * 清除所有缓存
     */
    public void clear() {
        // 清除所有缓存条目
        getCache().invalidateAll();
    }

    /**
     * 将字符串列表转换为指定类型的列表
     *
     * @param data   原始数据
     * @param target 目标类型类
     * @return 转换后的列表
     */
    protected <R> List<R> convertToList(List<String> data,
                                        Class<R> target) {
        // 使用 Optional 处理空值，流式转换
        return ofNullable(data)
                .stream()
                .map(str -> Convert.convert(target, str))
                .toList();
    }
}

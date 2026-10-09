package org.myframework.ai.helper;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import io.github.premocloud.typesafe.TypeSafeClient;
import lombok.Synchronized;
import org.myframework.core.exception.BusinessException;
import org.springframework.beans.BeansException;

/**
 * TypeSafe 客户端的静态入口，取不到 Bean 时统一抛 {@link BusinessException}
 *
 * <p>解析结果记在 {@link #resolved} 上，而不是靠 {@code client} 判断——失败时 client 恒为 null。</p>
 */
public abstract class AbstractTypeSafeHelper {

    /**
     * 首次访问时从 Spring 上下文取出，取到即固定
     */
    private static volatile TypeSafeClient client;

    /**
     * 是否已解析过；失败也置位，免得每次调用都重取 Bean
     */
    private static volatile boolean resolved;

    /**
     * 取全局唯一的 TypeSafeClient
     *
     * @throws BusinessException 未配置 {@code typesafe.api-key}，或本次解析尚未成功
     */
    public static TypeSafeClient getClient() {
        if (BooleanUtil.isFalse(resolved)) resolve(); // 已解析过就不用进锁
        if (ObjectUtil.isNotNull(client)) return client;
        throw new BusinessException("TypeSafe 未配置 API Key，无法使用智能问答");
    }

    /**
     * 解析并缓存：取到、或 Bean 确实没装配就缓存；瞬时异常不缓存，下次重试
     */
    @Synchronized
    private static void resolve() {
        if (BooleanUtil.isTrue(resolved)) return; // 双检：已经解析过就别白取一次 Bean
        try {
            client = SpringUtil.getBean(TypeSafeClient.class);
            StaticLog.trace("TypeSafe 客户端已注入");
            resolved = Boolean.TRUE;
        } catch (BeansException e) {
            // api-key 留空 → Bean 永远不会装配，缓存掉
            // BusinessException 不带 cause，原因只能进日志
            StaticLog.error("TypeSafe 客户端未装配: {}", e.getMessage());
            resolved = Boolean.TRUE;
        } catch (RuntimeException e) {
            // 上下文尚未就绪等瞬时失败，不缓存
            StaticLog.error("TypeSafe 客户端未就绪: {}", e.getMessage());
        }
    }
}

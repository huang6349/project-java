package org.myframework.ai.helper;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import io.github.premocloud.typesafe.TypeSafeClient;
import org.myframework.core.exception.BusinessException;

/**
 * TypeSafe 助手基类
 *
 * <p>持有全局唯一的 {@link TypeSafeClient}，首次访问时经 {@link SpringUtil} 惰性取出。
 * Bean 是否存在取决于 {@code typesafe.api-key}：留空时 starter 不装配，
 * 此时取 Bean 会抛出业务异常而非 NoSuchBeanDefinition，便于调用方统一兜底。</p>
 */
public abstract class AbstractTypeSafeHelper {

    private static volatile TypeSafeClient client;

    /**
     * 获取 TypeSafeClient
     *
     * @throws BusinessException 未配置 {@code typesafe.api-key}，客户端 Bean 未装配
     */
    public static TypeSafeClient getClient() {
        if (client == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractTypeSafeHelper.class) { // 同步锁
                if (client == null) { // 第二次检查，确保只初始化一次
                    try {
                        client = SpringUtil.getBean(TypeSafeClient.class);
                        StaticLog.trace("初始化完成，静态模板已注入");
                    } catch (Exception e) {
                        // BusinessException 无 (message, cause) 构造器，原异常只能走日志
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new BusinessException("TypeSafe 未配置 API Key，无法使用智能问答");
                    }
                }
            }
        }
        return client;
    }
}

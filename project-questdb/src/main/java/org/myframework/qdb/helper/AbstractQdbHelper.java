package org.myframework.qdb.helper;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.datasource.DataSourceKey;
import org.myframework.qdb.config.IlpConfig;

import java.util.function.Supplier;

/**
 * QuestDB 助手抽象基类
 * <p>
 * 提供 ILP 连接串单例的静态访问与数据源上下文切换，业务通过 {@link QdbHelper} 调用。
 */
public abstract class AbstractQdbHelper {

    private static volatile String ilpConfig;

    protected static String getIlpConfig() {
        if (ilpConfig == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractQdbHelper.class) {
                if (ilpConfig == null) { // 第二次检查，确保只初始化一次
                    try {
                        ilpConfig = SpringUtil.getBean(IlpConfig.class).value();
                        StaticLog.trace("初始化完成，静态模板已注入");
                    } catch (Exception e) {
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new RuntimeException("初始化失败", e);
                    }
                }
            }
        }
        return ilpConfig;
    }

    /**
     * 在 QuestDB 数据源上下文中执行 supplier，自动管理 DataSourceKey 切换
     */
    protected static <T> T runInQdb(Supplier<T> supplier) {
        try {
            DataSourceKey.use("questdb");
            return supplier.get();
        } finally {
            DataSourceKey.clear();
        }
    }
}

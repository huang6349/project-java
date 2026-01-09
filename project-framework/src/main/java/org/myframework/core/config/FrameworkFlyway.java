package org.myframework.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import org.dromara.autotable.core.callback.AutoTableFinishCallback;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Set;

/**
 * Flyway 数据库迁移配置类
 * <p>
 * 负责管理和执行数据库迁移脚本，确保数据库结构与代码模型保持同步。
 * <ul>
 *     <li>在 AutoTable 自动建表完成后执行迁移</li>
 *     <li>支持基线迁移，兼容已有数据库</li>
 *     <li>迁移前校验脚本有效性</li>
 * </ul>
 *
 * @author huangyalong
 * @see <a href="https://flywaydb.org/">Flyway Documentation</a>
 */
@Configuration
public class FrameworkFlyway implements AutoTableFinishCallback {

    private final Flyway flyway;

    /**
     * 构造函数，初始化 Flyway 实例
     * <p>
     * 配置说明：
     * <ul>
     *     <li>{@code baselineOnMigrate}: 对已有数据库启用基线，避免重复执行历史脚本</li>
     *     <li>{@code baselineVersion}: 基线版本号，低于此版本的迁移脚本将被忽略</li>
     *     <li>{@code validateOnMigrate}: 执行前校验迁移脚本有效性</li>
     *     <li>{@code cleanDisabled}: 禁用 clean 命令，防止生产环境误删数据</li>
     * </ul>
     *
     * @param dataSource 数据源，用于连接数据库执行迁移
     */
    public FrameworkFlyway(DataSource dataSource) {
        StaticLog.trace("初始化 Flyway 配置");
        flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(Boolean.TRUE)
                .baselineVersion("2026.01.01")
                .validateOnMigrate(Boolean.TRUE)
                .cleanDisabled(Boolean.FALSE)
                .load();
    }

    /**
     * AutoTable 回调方法，在自动建表完成后执行数据库迁移
     * <p>
     * 执行 {@code db/migration} 目录下的 SQL 脚本，完成数据初始化和更新。
     *
     * @param tableClasses 自动创建的表对应的实体类集合（此方法未使用，仅实现接口）
     */
    @Override
    public void finish(Set<Class<?>> tableClasses) {
        if (ObjectUtil.isNotNull(flyway)) {
            flyway.migrate();
        }
    }
}

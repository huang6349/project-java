package org.myframework.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import org.dromara.autotable.core.callback.AutoTableFinishCallback;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Set;

@Configuration
public class FrameworkFlyway implements AutoTableFinishCallback {

    private final Flyway flyway;

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

    @Override
    public void finish(Set<Class<?>> tableClasses) {
        if (ObjectUtil.isNotNull(flyway)) {
            flyway.migrate();
        }
    }
}

package org.myframework.core.config;

import cn.hutool.core.util.ObjectUtil;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter(FrameworkAutoTable.class)
public class FrameworkFlyway {

    private final Flyway flyway;

    public FrameworkFlyway(DataSource dataSource) {
        flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(Boolean.TRUE)
                .baselineVersion("2026.01.01")
                .validateOnMigrate(Boolean.TRUE)
                .cleanDisabled(Boolean.FALSE)
                .load();
    }

    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        if (ObjectUtil.isNotNull(flyway)) {
            flyway.migrate();
        }
    }
}

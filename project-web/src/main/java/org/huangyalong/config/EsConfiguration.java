package org.huangyalong.config;

import org.dromara.easyes.spring.annotation.EsMapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EsMapperScan("org.huangyalong.**.esmp")
public class EsConfiguration {
}

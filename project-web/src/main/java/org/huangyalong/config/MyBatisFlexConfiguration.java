package org.huangyalong.config;

import org.mybatis.spring.annotation.MapperScan;
import org.myframework.core.mybatisflex.MyBatisFlexConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.huangyalong.**.mapper")
public class MyBatisFlexConfiguration extends MyBatisFlexConfigurer {
}

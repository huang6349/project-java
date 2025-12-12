package org.myframework.core.config;

import org.myframework.extra.eventbus.BusHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BusHelper.class)
public class FrameworkEventbus {
}

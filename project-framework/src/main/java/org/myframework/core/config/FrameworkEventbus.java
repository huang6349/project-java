package org.myframework.core.config;

import org.myframework.core.eventbus.MsgHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MsgHelper.class)
public class FrameworkEventbus {
}

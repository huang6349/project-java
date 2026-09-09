package org.myframework.core.config;

import org.myframework.extra.notify.NotifySendService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(NotifySendService.class)
public class FrameworkNotify {
}

package org.myframework.extra.webhook.config;

import org.myframework.extra.webhook.helper.WebhookHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebhookHelper.class)
public class FrameworkWebhook {
}

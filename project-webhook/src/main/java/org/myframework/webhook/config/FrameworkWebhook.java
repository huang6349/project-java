package org.myframework.webhook.config;

import org.myframework.webhook.helper.WebhookHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebhookHelper.class)
public class FrameworkWebhook {
}

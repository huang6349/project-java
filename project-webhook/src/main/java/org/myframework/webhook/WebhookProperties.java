package org.myframework.webhook;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("app.webhook")
@ToString(callSuper = true)
public class WebhookProperties implements Serializable {

    private boolean enabled = Boolean.TRUE;
}

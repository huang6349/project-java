package org.myframework.ai.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("app.ai")
@ToString(callSuper = true)
public class AiProperties implements Serializable {

    private boolean enabled = Boolean.FALSE;
}

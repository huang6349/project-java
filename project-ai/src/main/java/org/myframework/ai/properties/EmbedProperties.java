package org.myframework.ai.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("agents-flex.embed")
@ToString(callSuper = true)
public class EmbedProperties implements Serializable {

    private String model = "text-embedding-v1";

    private String endpoint = "https://dashscope.aliyuncs.com";

    private String apiKey;

    private String apiSecret;
}

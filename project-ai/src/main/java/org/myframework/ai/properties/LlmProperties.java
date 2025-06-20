package org.myframework.ai.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("agents-flex.llm")
@ToString(callSuper = true)
public class LlmProperties implements Serializable {

    private String model = "qwen-turbo";

    private String endpoint = "https://dashscope.aliyuncs.com";

    private String apiKey;

    private String apiSecret;
}

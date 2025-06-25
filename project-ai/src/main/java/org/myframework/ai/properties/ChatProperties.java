package org.myframework.ai.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("solon.ai.chat")
@ToString(callSuper = true)
public class ChatProperties implements Serializable {

    private String apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    private String apiKey;

    private String provider = "dashscope";

    private String model = "qwen-turbo";
}

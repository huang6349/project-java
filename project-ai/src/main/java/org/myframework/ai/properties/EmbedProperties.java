package org.myframework.ai.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("app.ai.embed")
@ToString(callSuper = true)
public class EmbedProperties implements Serializable {

    private String apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/embeddings/text-embedding/text-embedding";

    private String apiKey;

    private String provider = "dashscope";

    private String model = "text-embedding-v3";
}

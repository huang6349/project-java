package org.myframework.ai.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("solon.ai.qdrant")
@ToString(callSuper = true)
public class QdrantProperties implements Serializable {

    private String host;

    private Integer port = 6334;
}

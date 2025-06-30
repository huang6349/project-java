package org.myframework.core.swagger;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("app.swagger")
@ToString(callSuper = true)
public class SwaggerProperties implements Serializable {

    private String title = "在线文档";

    private String description = "";

    private String contactName = "";

    private String version = "1.0";
}

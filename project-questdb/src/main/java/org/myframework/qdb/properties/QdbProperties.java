package org.myframework.qdb.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("app.qdb")
@ToString(callSuper = true)
public class QdbProperties implements Serializable {

    /**
     * QuestDB 服务地址
     */
    private String host = "localhost";

    /**
     * ILP 写入端口（默认 9000）
     */
    private Integer port = 9000;

    /**
     * ILP 用户名
     */
    private String username;

    /**
     * ILP 密码
     */
    private String password;
}

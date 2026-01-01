package org.huangyalong.modules.system.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties("app.tenant")
@ToString(callSuper = true)
public class TenantProperties implements Serializable {

    /**
     * 租户功能是否开启
     */
    private boolean enabled = Boolean.TRUE;
}

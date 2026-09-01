package org.myframework.iot.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * IoT 配置属性
 *
 * <p>绑定配置前缀 {@code app.iot}，经自动装配注册
 * （{@code META-INF/spring.factories} 与 {@code AutoConfiguration.imports}），
 * 由 {@link org.myframework.iot.DeviceGatewayProvider} 读取 {@link #isEnabled()} 开关以控制网关组件的启动。</p>
 *
 * @see org.myframework.iot.DeviceGatewayProvider
 */
@Data
@Configuration
@ConfigurationProperties("app.iot")
@ToString(callSuper = true)
public class IotProperties implements Serializable {

    /**
     * IoT 网关组件是否启动，默认开启
     */
    private boolean enabled = Boolean.TRUE;
}

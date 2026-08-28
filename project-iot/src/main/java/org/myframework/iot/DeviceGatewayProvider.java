package org.myframework.iot;

import cn.hutool.core.util.ServiceLoaderUtil;
import com.iteaj.iot.FrameworkManager;
import org.myframework.iot.properties.IotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;

/**
 * 设备网关启动器
 *
 * <p>监听 {@link ApplicationReadyEvent} 事件，在应用就绪后自动注册并启动所有协议组件。
 * 通过 {@link IotProperties#isEnabled()} 开关控制是否启用 IoT 服务（默认开启），
 * 依赖 {@code frameworkReadyListener} 确保框架初始化完成后再启动组件。</p>
 *
 * <p>使用 Hutool {@link ServiceLoaderUtil} 加载 {@link DeviceProtocolSupplier} 实现；
 * 各供应商在自身模块的 {@code META-INF/services/org.myframework.iot.DeviceProtocolSupplier}
 * 文件中声明，服务加载器会自动合并 classpath 上所有同名注册文件。</p>
 *
 * @see DeviceProtocolSupplier
 * @see IotProperties
 */
@DependsOn("frameworkReadyListener")
public class DeviceGatewayProvider implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private IotProperties iotProperties;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        if (!iotProperties.isEnabled()) return;
        var loader = ServiceLoaderUtil.load(DeviceProtocolSupplier.class);
        for (DeviceProtocolSupplier<?> supplier : loader) {
            var component = supplier.createComponent();
            FrameworkManager.start(component);
        }
    }
}

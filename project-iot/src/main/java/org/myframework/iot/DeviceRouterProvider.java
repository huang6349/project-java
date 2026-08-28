package org.myframework.iot;

import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.myframework.iot.properties.IotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;

/**
 * 设备路由注册器
 * <p>
 * 监听 {@link ApplicationReadyEvent}，在应用就绪后通过
 * {@link ServiceLoaderUtil} 加载 {@link AbstractDeviceRouter} 实现并注册到 CamelContext。
 * 各协议模块在自身 {@code META-INF/services/org.myframework.iot.AbstractDeviceRouter}
 * 文件中声明实现类，服务加载器自动合并 classpath 上所有同名注册文件。
 * <p>
 * 通过自动配置注册（见 {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}），
 * 由 {@link IotProperties#isEnabled()} 开关控制是否启用。
 *
 * @see AbstractDeviceRouter
 */
@DependsOn("frameworkReadyListener")
public class DeviceRouterProvider implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private IotProperties iotProperties;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        if (!iotProperties.isEnabled()) return;
        var loader = ServiceLoaderUtil.load(AbstractDeviceRouter.class);
        loader.forEach(this::addRoutes);
    }

    /**
     * 注册单条设备路由（{@code @SneakyThrows} 将受检异常转为非受检异常抛出，注册失败会导致应用终止）
     */
    @SneakyThrows
    void addRoutes(AbstractDeviceRouter router) {
        SpringUtil.getBean(CamelContext.class)
                .addRoutes(router);
    }
}

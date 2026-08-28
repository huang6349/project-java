package org.myframework.iot;

import org.apache.camel.builder.RouteBuilder;

/**
 * 设备报文路由基类
 * <p>
 * 各协议模块的路由统一继承本类（保持类名 {@code DeviceRouter} 便于识别），
 * 通过 SPI 机制注册：在 {@code META-INF/services/org.myframework.iot.AbstractDeviceRouter}
 * 文件中声明实现类全限定名，由 {@link DeviceRouterProvider} 在应用就绪后统一加载并注册。
 *
 * @see DeviceRouterProvider
 */
public abstract class AbstractDeviceRouter extends RouteBuilder {
}

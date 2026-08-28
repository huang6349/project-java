package org.myframework.iot;

import com.iteaj.iot.FrameworkComponent;
import org.myframework.iot.protocol.ProtocolModel;

/**
 * 设备协议供应商
 *
 * <p>IoT 设备协议接入的顶层抽象。每个设备协议由对应的供应商实现，
 * 负责提供协议组件实例、已创建的组件引用以及协议模型。</p>
 *
 * <p>实现类通过 SPI 机制注册：在 {@code META-INF/services/org.myframework.iot.DeviceProtocolSupplier}
 * 文件中声明全限定类名，由 {@link DeviceGatewayProvider} 在应用就绪后统一加载并启动。</p>
 *
 * @param <T> 协议组件类型，须继承 {@link FrameworkComponent}
 * @see DeviceGatewayProvider
 * @see StaticProtocolSupplier
 */
public interface DeviceProtocolSupplier<T extends FrameworkComponent> {

    /**
     * 获取协议组件实例，首次调用时创建并初始化。
     *
     * @return 协议组件实例；同一供应商的多次调用返回同一实例
     */
    T createComponent();

    /**
     * 获取已创建的协议组件实例。
     *
     * @return 已创建的组件实例；尚未创建时返回 {@code null}
     */
    T getComponent();

    /**
     * 获取协议元数据模型。
     *
     * @return 协议模型，用于标识协议的唯一编码
     */
    ProtocolModel getProtocol();
}

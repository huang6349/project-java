package org.myframework.iot;

import com.iteaj.iot.server.ServerComponent;

/**
 * 服务端协议供应商
 *
 * <p>继承 {@link DeviceProtocolSupplier}，限定组件类型为 {@link ServerComponent}。
 * 用于标识基于 TCP/UDP 的服务端协议供应商，
 * 与 {@link StaticServerProtocolSupplier} 配合实现懒加载注册。</p>
 *
 * @param <T> 服务端组件类型，须继承 {@link ServerComponent}
 * @see StaticServerProtocolSupplier
 */
public interface ServerProtocolSupplier<T extends ServerComponent> extends DeviceProtocolSupplier<T> {
}

package org.myframework.iot;

import com.iteaj.iot.server.ServerComponent;

/**
 * 静态服务端协议供应商
 *
 * <p>组合 {@link StaticProtocolSupplier} 的组件懒加载与注册能力，以及
 * {@link ServerProtocolSupplier} 的服务端组件标记。具体设备协议供应商继承本类后，
 * 只需实现 {@link #doCreateComponent()} 提供组件实例。</p>
 *
 * @param <T> 服务端组件类型，须继承 {@link ServerComponent}
 */
public abstract class StaticServerProtocolSupplier<T extends ServerComponent>
        extends StaticProtocolSupplier<T>
        implements ServerProtocolSupplier<T> {
}

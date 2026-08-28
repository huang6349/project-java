package org.myframework.iot.protocol;

/**
 * 协议模型
 *
 * <p>定义协议的元数据模型，用于标识协议的唯一编码。
 * 每个协议供应商通过 {@link org.myframework.iot.DeviceProtocolSupplier#getProtocol()}
 * 返回对应的协议模型实例。</p>
 *
 * @see DefaultProtocolModel
 * @see org.myframework.iot.DeviceProtocolSupplier
 */
public interface ProtocolModel {

    /**
     * 获取协议唯一编码。
     *
     * @return 协议编码，如 {@code ZXHBJQ}
     */
    String getCode();
}

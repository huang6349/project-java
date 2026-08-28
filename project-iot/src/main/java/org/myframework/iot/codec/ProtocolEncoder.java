package org.myframework.iot.codec;

import com.iteaj.iot.server.ServerMessage;

/**
 * 协议编码器
 *
 * <p>负责根据设备编码构建响应消息。
 * 通常用于服务端向设备发送确认、控制指令等响应。</p>
 *
 * @param <M> 响应消息类型，须继承 {@link ServerMessage}
 */
public interface ProtocolEncoder<M extends ServerMessage> {

    /**
     * 根据设备编码构建响应消息。
     *
     * @param equipCode 设备编码
     * @return 待下发给设备的响应消息
     */
    M encode(String equipCode);
}

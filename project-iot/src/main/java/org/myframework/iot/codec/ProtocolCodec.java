package org.myframework.iot.codec;

import com.iteaj.iot.server.ServerMessage;

/**
 * 协议编解码器
 *
 * <p>组合 {@link ProtocolEncoder} 和 {@link ProtocolDecoder} 接口，
 * 提供协议消息的双向转换能力。具体协议实现此类，
 * 定义报文的解码（字节→消息对象）和编码（设备编码→响应消息）逻辑。</p>
 *
 * @param <M> 响应消息类型，须继承 {@link ServerMessage}
 * @see ProtocolEncoder
 * @see ProtocolDecoder
 */
public interface ProtocolCodec<M extends ServerMessage> extends ProtocolEncoder<M>, ProtocolDecoder {
}

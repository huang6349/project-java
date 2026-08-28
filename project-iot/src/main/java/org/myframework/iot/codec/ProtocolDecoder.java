package org.myframework.iot.codec;

import org.myframework.iot.message.DecoderMessage;

/**
 * 协议解码器
 *
 * <p>负责将设备上报的字节报文解码为 {@link DecoderMessage} 消息对象。
 * 解析过程通常包括：提取设备编号、解析业务字段（温度、电压等）、
 * 构建标准化的消息结构。</p>
 *
 * <p>实现约定：报文为 {@code null}、空数组或无法识别格式时返回 {@code null}
 * 表示忽略该报文，而非抛出异常。</p>
 *
 * @see DecoderMessage
 */
public interface ProtocolDecoder {

    /**
     * 将设备上报的原始字节报文解码为标准化消息。
     *
     * @param message 设备上报的原始报文
     * @return 解码后的消息对象；报文非法或无法识别时返回 {@code null}
     */
    DecoderMessage decode(byte[] message);
}

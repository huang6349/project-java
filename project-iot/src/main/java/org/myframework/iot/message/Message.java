package org.myframework.iot.message;

import java.io.Serializable;

/**
 * IoT 消息接口
 *
 * <p>所有 IoT 协议消息的顶层标记接口，继承 {@link Serializable} 以支持序列化。
 * 具体消息类型（如 {@link DecoderMessage}）实现此接口，
 * 统一消息体系的类型约束。</p>
 *
 * @see DecoderMessage
 */
public interface Message extends Serializable {
}

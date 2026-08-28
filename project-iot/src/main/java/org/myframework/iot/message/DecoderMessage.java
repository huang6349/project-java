package org.myframework.iot.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 解码消息
 *
 * <p>协议解码器 {@link org.myframework.iot.codec.ProtocolDecoder} 的输出对象，
 * 承载解码后的设备编号与业务属性，是协议解析层与业务层之间的数据载体。</p>
 *
 * <p>通过静态工厂 {@link #create()} 构建，配合链式 setter 设置字段。</p>
 *
 * @see org.myframework.iot.codec.ProtocolDecoder
 */
@Accessors(chain = true)
@Data(staticConstructor = "create")
public class DecoderMessage implements Message {

    @Schema(description = "设备编号")
    private String deviceId;

    @Schema(description = "设备属性")
    private Map<String, Object> properties;
}

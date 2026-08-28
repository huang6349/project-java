package org.myframework.iot.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认协议模型
 *
 * <p>{@link ProtocolModel} 接口的默认实现，使用 Lombok 注解简化代码。
 * 通过 {@code @Builder} 支持 Builder 模式创建，
 * 通过 {@code @Data} 自动生成 getter/setter/toString 等方法。</p>
 *
 * @see ProtocolModel
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultProtocolModel implements ProtocolModel {

    /**
     * 协议唯一编码
     */
    private String code;
}

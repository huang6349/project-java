package org.myframework.extra.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookMessage implements Serializable {

    // 事件类型
    private String eventType;

    // 事件数据
    private Object data;

    // 事件编号
    private String eventId;
}

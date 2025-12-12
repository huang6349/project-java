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
public class WebhookResponse implements Serializable {

    // 是否成功
    private Boolean success;

    // 响应内容
    private String data;

    // 错误信息
    private String message;

    // 响应状态码
    private Integer code;

    // 请求编号
    private String deliveryId;
}

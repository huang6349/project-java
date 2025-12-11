package org.myframework.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookRequest implements Serializable {

    // 请求地址
    private String url;

    // 事件类型
    private String eventType;

    // 请求数据
    private Object data;

    // 数据格式：JSON 或 表单
    private WebhookFormat format;

    // 签名密钥（可选，不传则使用默认密钥）
    private String secret;

    // 自定义头信息
    private Map<String, String> headers;

    // 超时时间（毫秒，可选）
    private Integer timeout;
}

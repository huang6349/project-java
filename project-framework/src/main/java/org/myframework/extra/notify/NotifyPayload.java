package org.myframework.extra.notify;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * 消息负载(渠道处理器与记录回写共用)
 */
public interface NotifyPayload extends Serializable {

    @Schema(description = "来源应用")
    String getApp();

    @Schema(description = "消息地址")
    String getRecipient();

    @Schema(description = "消息渠道")
    default String getChannel() {
        var tple = getTple();
        var recip = getRecipient();
        return format("{}:{}", tple, recip);
    }

    @Schema(description = "消息模板")
    String getTple();

    @Schema(description = "消息编号")
    String getId();

    @Schema(description = "消息内容")
    String getContent();
}

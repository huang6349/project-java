package org.huangyalong.extra.notify.processor;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;

import java.util.Map;

import static cn.hutool.core.lang.Validator.validateNotEmpty;

/**
 * 控制台消息处理器(示例渠道,演示 SPI 注册)
 * <p>
 * 通过 {@code META-INF/services/org.myframework.extra.notify.NotifyProcessor}
 * 注册,注册键 {@code consoleProcessor}(类名首字母小写),
 * 发送方 {@code NotifySend.code} 传 {@code console}。
 * 真实渠道(短信/邮件等)照此结构实现并各自 SPI 注册
 */
public class ConsoleProcessor extends AbstractNotifyProcessor<ConsolePayload> {

    @Override
    public ConsolePayload getPayload(Map<String, Object> message) {
        var json = JSONUtil.parseObj(message);
        return ConsolePayload.create()
                .setApp(json.getStr("app"))
                .setRecipient(json.getStr("recipient"))
                .setId(json.getStr("id"))
                .setContent(json.getStr("content"));
    }

    @Override
    public void send(ConsolePayload payload) {
        validateNotEmpty(payload.getRecipient(), "无效的消息参数[recipient]，请核实！");
        validateNotEmpty(payload.getId(), "无效的消息参数[id]，请核实！");
        validateNotEmpty(payload.getContent(), "无效的消息参数[content]，请核实！");
        StaticLog.info("控制台渠道发送消息: {}", payload);
    }
}

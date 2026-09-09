package org.myframework.extra.notify;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息发送门面(静态入口)
 */
public class NotifySendHelper extends AbstractNotifySendHelper {

    @SneakyThrows
    public static void send(Class<? extends NotifyProcessor<?>> clazz,
                            NotifyMessage message) {
        StaticLog.trace("发送消息: {}", message.getData());
        getService().send(clazz, message);
    }

    @SneakyThrows
    public static void send(String name, Map<String, Object> message) {
        StaticLog.trace("发送消息: {}", message);
        getService().send(name, message);
    }

    @SneakyThrows
    public static void send(NotifySend send) {
        var app = Opt.ofNullable(send)
                .map(NotifySend::getApp)
                .get();
        var recipient = Opt.ofNullable(send)
                .map(NotifySend::getRecipient)
                .get();
        var id = Opt.ofNullable(send)
                .map(NotifySend::getId)
                .get();
        var message = Opt.ofNullable(send)
                .map(NotifySend::getExtras)
                .map(HashMap::new)
                .orElseGet(HashMap::new);
        message.put("app", app);
        message.put("recipient", recipient);
        message.put("id", id);
        var name = processorName(send);
        send(name, message);
    }

    /**
     * 取处理器实例的注册键:实现类简单名首字母小写
     * (如 {@code ConsoleProcessor -> consoleProcessor})
     */
    public static String processorName(NotifyProcessor<? extends NotifyPayload> processor) {
        return Opt.ofNullable(processor)
                .map(ClassUtil::getClass)
                .map(NotifySendHelper::processorName)
                .get();
    }

    /**
     * 取处理器类型的注册键:实现类简单名首字母小写
     * (如 {@code ConsoleProcessor -> consoleProcessor})
     */
    public static String processorName(Class<? extends NotifyProcessor<?>> clazz) {
        return Opt.ofNullable(clazz)
                .map(Class::getSimpleName)
                .map(StrUtil::lowerFirst)
                .get();
    }

    /**
     * 取发送请求的注册键:渠道代码还原
     * <p>
     * code 传处理器类名去除 Processor 后缀的小驼峰(如 {@code ConsoleProcessor -> console}),
     * 内部补回 Processor 后缀,与 {@link #processorName(Class)} 对同一渠道产出一致
     */
    public static String processorName(NotifySend send) {
        var code = Opt.ofNullable(send)
                .map(NotifySend::getCode)
                .map(StrUtil::lowerFirst)
                .get();
        return code + "Processor";
    }
}

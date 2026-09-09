package org.myframework.extra.notify;

import java.util.Map;

/**
 * 消息处理器(渠道组件)
 * <p>
 * 实现类通过 SPI 机制注册：在 {@code META-INF/services/org.myframework.extra.notify.NotifyProcessor}
 * 文件中声明全限定名,由 {@link NotifySendService} 装载。
 * 注册键为实现类简单名首字母小写,如 {@code ConsoleProcessor -> consoleProcessor};
 * 发送方 {@link NotifySend#getCode()} 传类名去除 {@code Processor} 后缀的小驼峰
 * (如 {@code console}),门面按 {@code code + "Processor"} 还原注册键
 */
public interface NotifyProcessor<T extends NotifyPayload> {

    T getPayload(Map<String, Object> message);

    void recordFreq(T message);

    default boolean checkFreq(T message) {
        // 默认不限频
        return Boolean.TRUE;
    }

    void send(Map<String, Object> message) throws Exception;

    void send(T message) throws Exception;
}

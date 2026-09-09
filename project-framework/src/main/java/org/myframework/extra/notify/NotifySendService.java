package org.myframework.extra.notify;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ServiceLoaderUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static cn.hutool.core.lang.Validator.validateNotEmpty;
import static cn.hutool.core.lang.Validator.validateNotNull;
import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * 消息发送服务(SPI 装载,Spring 单例)
 * <p>
 * 处理器经 SPI 机制注册加载：
 * 各渠道实现类在自身模块的 {@code META-INF/services/org.myframework.extra.notify.NotifyProcessor}
 * 文件中声明,服务加载器自动合并 classpath 上所有同名注册文件。
 * 注册键为实现类简单名首字母小写,如 {@code ConsoleProcessor -> consoleProcessor},
 * 与 {@link NotifySendHelper} 按 {@code 渠道代码 + "Processor"} 的解析结果对应
 * ({@link NotifySend#getCode()} 传类名去除 Processor 后缀的小驼峰,如 {@code console})。
 * <p>
 * 由框架 {@code FrameworkNotify} 经 {@code @Import} 注册为单例 Bean,
 * 门面经 {@link AbstractNotifySendHelper#getService()} 懒加载持有
 */
public class NotifySendService {

    private final Map<String, NotifyProcessor<? extends NotifyPayload>> processorMap = new LinkedHashMap<>();

    public NotifySendService() {
        var loader = ServiceLoaderUtil.load(NotifyProcessor.class);
        for (NotifyProcessor<? extends NotifyPayload> processor : loader) {
            var name = NotifySendHelper.processorName(processor);
            processorMap.put(name, processor);
        }
    }

    public void send(Class<? extends NotifyProcessor<?>> clazz,
                     NotifyMessage message) throws Exception {
        var data = Opt.ofNullable(message)
                .map(NotifyMessage::getData)
                .get();
        send(clazz, data);
    }

    public void send(Class<? extends NotifyProcessor<?>> clazz,
                     Map<String, Object> message) throws Exception {
        var name = NotifySendHelper.processorName(clazz);
        send(name, message);
    }

    public void send(String name, Map<String, Object> message) throws Exception {
        validateNotEmpty(message, "无效的消息参数，请核实！");
        var processor = processorMap.get(name);
        validateNotNull(processor, format("消息处理器 {} 不存在", name));
        processor.send(message);
    }
}

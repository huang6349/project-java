package org.myframework.core.eventbus;

import cn.hutool.log.StaticLog;
import com.github.likavn.eventbus.core.api.MsgDelayListener;
import com.github.likavn.eventbus.core.api.MsgListener;
import com.github.likavn.eventbus.core.metadata.data.MsgBody;

public class BusHelper extends AbstractBusHelper {

    /* --------------------延时消息------------------------ */

    public static void sendDelayMessage(MsgBody body, long delayTime) {
        StaticLog.trace("发送消息: {} ，并设置延迟时间: {}", body, delayTime);
        getSender().sendDelayMessage(body, delayTime);
    }

    public static void sendDelayMessage(Class<? extends MsgDelayListener<?>> handlerClz, Object body, long delayTime) {
        StaticLog.trace("发送消息: {} ，并设置延迟时间: {}", body, delayTime);
        getSender().sendDelayMessage(handlerClz, body, delayTime);
    }

    public static void sendDelayMessage(String code, Object body, long delayTime) {
        StaticLog.trace("发送消息: {} ，并设置延迟时间: {}", body, delayTime);
        getSender().sendDelayMessage(code, body, delayTime);
    }

    /* --------------------实时消息------------------------ */

    public static void send(MsgBody body) {
        StaticLog.trace("发送消息: {}", body);
        getSender().send(body);
    }

    public static void send(Class<? extends MsgListener<?>> handlerClz, Object body) {
        StaticLog.trace("发送消息: {}", body);
        getSender().send(handlerClz, body);
    }

    public static void send(String code, Object body) {
        StaticLog.trace("发送消息: {}", body);
        getSender().send(code, body);
    }
}

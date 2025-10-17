package org.myframework.core.eventbus;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import com.github.likavn.eventbus.core.api.MsgSender;

public abstract class AbstractBusHelper {

    private static volatile MsgSender sender;

    protected static MsgSender getSender() {
        if (sender == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractBusHelper.class) { // 同步锁
                if (sender == null) { // 第二次检查，确保只初始化一次
                    try {
                        sender = SpringUtil.getBean(MsgSender.class);
                        StaticLog.trace("初始化完成，静态模板已注入");
                    } catch (Exception e) {
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new RuntimeException("初始化失败", e);
                    }
                }
            }
        }
        return sender;
    }
}

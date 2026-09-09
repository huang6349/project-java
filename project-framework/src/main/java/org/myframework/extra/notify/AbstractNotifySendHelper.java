package org.myframework.extra.notify;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;

/**
 * 发送门面基类:懒加载持有 {@link NotifySendService} 单例
 */
public abstract class AbstractNotifySendHelper {

    private static volatile NotifySendService service;

    protected static NotifySendService getService() {
        if (service == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractNotifySendHelper.class) {
                if (service == null) { // 第二次检查，确保只初始化一次
                    try {
                        service = SpringUtil.getBean(NotifySendService.class);
                        StaticLog.trace("初始化完成，NotifySendService 已注入");
                    } catch (Exception e) {
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new RuntimeException("初始化失败", e);
                    }
                }
            }
        }
        return service;
    }
}

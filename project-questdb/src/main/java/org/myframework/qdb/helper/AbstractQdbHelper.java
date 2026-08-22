package org.myframework.qdb.helper;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import io.questdb.client.QuestDB;

/**
 * QuestDB 助手抽象基类
 * <p>
 * 提供 {@link QuestDB} 单例的静态访问，业务通过 {@link QdbHelper} 调用。
 */
abstract class AbstractQdbHelper {

    private static volatile QuestDB questDB;

    protected static QuestDB getQuestDB() {
        if (questDB == null) { // 第一次检查，避免不必要的同步
            synchronized (AbstractQdbHelper.class) {
                if (questDB == null) { // 第二次检查，确保只初始化一次
                    try {
                        questDB = SpringUtil.getBean(QuestDB.class);
                        StaticLog.trace("初始化完成，静态模板已注入");
                    } catch (Exception e) {
                        StaticLog.error("初始化失败: {}", e.getMessage());
                        throw new RuntimeException("初始化失败", e);
                    }
                }
            }
        }
        return questDB;
    }
}

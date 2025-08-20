package org.myframework.core.eventbus;

import cn.hutool.log.StaticLog;
import com.github.likavn.eventbus.core.api.MsgSender;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

public abstract class AbstractMsgHelper {

    protected static MsgSender sender;

    @Resource
    private MsgSender msgSender;

    @PostConstruct
    void init() {
        sender = msgSender;
        StaticLog.trace("MsgHelper 初始化完成，静态模板已注入");
    }
}

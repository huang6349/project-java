package org.myframework.test;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.log.StaticLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public abstract class MyFrameworkTest {

    protected final Long DEFAULT_LOGIN = 10000000000000000L;

    @BeforeEach
    @Order(Integer.MIN_VALUE)
    void initTest() {
        StpUtil.login(DEFAULT_LOGIN);
        StaticLog.trace("InitTest 初始化完成，测试用户已注入");
    }
}

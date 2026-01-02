package org.myframework.core.satoken.helper;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import org.myframework.core.satoken.util.ContextUtil;

public abstract class ContextHelper {

    public static String getLoginIdAsString() {
        return Opt.ofNullable(getLoginId())
                .map(Convert::toStr)
                .get();
    }

    public static Long getLoginIdAsLong() {
        return Opt.ofNullable(getLoginId())
                .map(Convert::toLong)
                .get();
    }

    public static Object getLoginId() {
        if (SpringMVCUtil.isWeb()) {
            return StpUtil.getLoginIdDefaultNull();
        } else return ContextUtil.getLoginId();
    }

    public static String getToken() {
        if (SpringMVCUtil.isWeb()) {
            return StpUtil.getTokenValue();
        } else return ContextUtil.getToken();
    }
}

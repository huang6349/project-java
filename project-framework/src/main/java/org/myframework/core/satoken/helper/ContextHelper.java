package org.myframework.core.satoken.helper;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import org.myframework.core.satoken.util.ContextUtil;

@SuppressWarnings("unused")
public interface ContextHelper {

    static String getLoginIdAsString() {
        return Opt.ofBlankAble(getLoginId())
                .map(Convert::toStr)
                .get();
    }

    static Long getLoginIdAsLong() {
        return Opt.ofBlankAble(getLoginId())
                .map(Convert::toLong)
                .get();
    }

    static Object getLoginId() {
        if (SpringMVCUtil.isWeb()) {
            return StpUtil.getLoginIdDefaultNull();
        } else return ContextUtil.getLoginId();
    }
}

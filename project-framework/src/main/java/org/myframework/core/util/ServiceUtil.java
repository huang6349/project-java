package org.myframework.core.util;

import cn.hutool.core.date.DateTime;

import static cn.hutool.core.date.DatePattern.PURE_DATETIME_MS_FORMAT;

public final class ServiceUtil {

    public static String randomCode() {
        var date = DateTime.now();
        return PURE_DATETIME_MS_FORMAT.format(date);
    }
}

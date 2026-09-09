package org.huangyalong.extra.notify.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.huangyalong.extra.notify.FreqKey;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.lang.Opt.ofBlankAble;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static cn.hutool.core.text.CharSequenceUtil.subAfter;
import static cn.hutool.core.text.CharSequenceUtil.subBefore;

/**
 * 消息频次助手(静态门面,同包被处理器/发送链调用)
 */
@UtilityClass
public class NotifyHelper {

    public static final String DEFAULT_APP_NAME = "内置应用";

    public static final String DEFAULT_APP = "default";

    static final String SEPARATOR = "-";

    static final String EMPTY = null;

    public static Integer getFreq(Object message) {
        var key = format("notify:freq:{}", message);
        if (!RedisHelper.hasKey(key))
            NotifyFreqHelper.load(message);
        return ofBlankAble(RedisHelper.get(key))
                .map(Convert::toInt)
                .get();
    }

    public static Integer getFreq(FreqKey freqKey) {
        return Opt.ofNullable(freqKey)
                .map(NotifyHelper::toKey)
                .map(NotifyHelper::getFreq)
                .get();
    }

    static String toKey(FreqKey freqKey) {
        var tple = Opt.ofNullable(freqKey)
                .map(FreqKey::getTple)
                .get();
        var app = Opt.ofNullable(freqKey)
                .map(FreqKey::getApp)
                .get();
        return format("{}{}{}", tple, SEPARATOR, app);
    }

    public static String parseTple(String key) {
        if (StrUtil.isNotBlank(key)) {
            return subBefore(key, SEPARATOR, Boolean.TRUE);
        } else return EMPTY;
    }

    public static String parseApp(String key) {
        if (StrUtil.isNotBlank(key)) {
            return subAfter(key, SEPARATOR, Boolean.TRUE);
        } else return EMPTY;
    }
}

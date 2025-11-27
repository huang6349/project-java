package org.huangyalong.modules.notify.helper;

import cn.hutool.core.convert.Convert;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.lang.Opt.ofBlankAble;
import static cn.hutool.core.text.CharSequenceUtil.format;

public final class NotifyHelper {

    public static Integer getFreq(Object message) {
        var key = format("notify:freq:{}", message);
        if (!RedisHelper.hasKey(key))
            NotifyFreqHelper.load(message);
        return ofBlankAble(RedisHelper.get(key))
                .map(Convert::toInt)
                .get();
    }
}

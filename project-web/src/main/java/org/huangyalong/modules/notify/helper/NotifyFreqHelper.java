package org.huangyalong.modules.notify.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.modules.notify.domain.CategoryConfigs.NAME_FREQ;
import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public final class NotifyFreqHelper {

    static final Integer EMPTY = null;

    public static Integer fetch(Object code) {
        if (ObjectUtil.isNotEmpty(code)) {
            return QueryChain.of(NotifyCategory.class)
                    .select(ue(NOTIFY_CATEGORY.CONFIGS, NAME_FREQ))
                    .where(NOTIFY_CATEGORY.CODE.eq(code))
                    .oneAs(Integer.class);
        } else return EMPTY;
    }

    public static void load(Object code) {
        if (ObjectUtil.isNotEmpty(code)) {
            var key = format("notify:freq:{}", code);
            RedisHelper.delete(key);
            var freq = ofNullable(fetch(code))
                    .map(Convert::toStr)
                    .get();
            if (ObjectUtil.isNotEmpty(freq))
                RedisHelper.set(key, freq);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

package org.huangyalong.extra.notify.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.extra.notify.helper.NotifyHelper.DEFAULT_APP;
import static org.huangyalong.modules.notify.domain.CategoryConfigs.NAME_FREQ;
import static org.huangyalong.modules.notify.domain.table.NotifyAppTableDef.NOTIFY_APP;
import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

/**
 * 消息频次加载(静态查询配置)
 * <p>
 * 键支持两种形态：类别代码(查类别配置)与 {@code 模板-应用}(按应用配置,应用缺省为内置应用时回落类别)
 */
@UtilityClass
public class NotifyFreqHelper {

    static final Integer EMPTY = null;

    public static Integer fetch(Object code) {
        if (ObjectUtil.isNotEmpty(code)) {
            var key = Convert.toStr(code);
            var tple = NotifyHelper.parseTple(key);
            var app = NotifyHelper.parseApp(key);
            if (StrUtil.isNotEmpty(app)) {
                if (ObjectUtil.equal(app, DEFAULT_APP)) {
                    return fetchByCategory(tple);
                } else return fetchByApp(key);
            } else return fetchByCategory(key);
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

    static Integer fetchByCategory(Object tple) {
        return QueryChain.of(NotifyCategory.class)
                .select(ue(NOTIFY_CATEGORY.CONFIGS, NAME_FREQ))
                .where(NOTIFY_CATEGORY.CODE.eq(tple))
                .oneAs(Integer.class);
    }

    static Integer fetchByApp(Object app) {
        return QueryChain.of(NotifyApp.class)
                .select(ue(NOTIFY_APP.CONFIGS, NAME_FREQ))
                .where(NOTIFY_APP.CODE.eq(app))
                .oneAs(Integer.class);
    }
}

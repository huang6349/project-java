package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.system.domain.System;
import org.myframework.core.redis.RedisHelper;

import static cn.hutool.core.lang.Opt.ofBlankAble;
import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static java.util.concurrent.TimeUnit.HOURS;
import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;
import static org.huangyalong.modules.system.configs.TenantConfigs.NAME_ENABLED;
import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;

@UtilityClass
public class SystemHelper {

    private static final String KEY = format("system:configs:{}", CONFIG_ID);

    public static boolean isTenantEnabled() {
        var enabled = ofBlankAble(getConfigs())
                .map(JSONUtil::parseObj)
                .orElseGet(JSONUtil::createObj)
                .getByPath(NAME_ENABLED, Boolean.class);
        return ofNullable(enabled)
                .orElse(Boolean.TRUE);
    }

    private static String getConfigs() {
        var value = RedisHelper.get(KEY);
        if (StrUtil.isBlank(value)) {
            load();
            return RedisHelper.get(KEY);
        } else return value;
    }

    public static String fetch(Object id) {
        return QueryChain.of(System.class)
                .select(SYSTEM.CONFIGS)
                .where(SYSTEM.ID.eq(id))
                .oneAs(String.class);
    }

    public static void load() {
        RedisHelper.delete(KEY);
        var configs = fetch(CONFIG_ID);
        if (ObjectUtil.isNotEmpty(configs))
            RedisHelper.set(KEY, configs);
        RedisHelper.expire(KEY, 24, HOURS);
    }
}

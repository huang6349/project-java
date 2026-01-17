package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.System;
import org.myframework.core.helper.FetchLoadHelper;

import java.io.Serializable;

import static cn.hutool.core.lang.Opt.ofBlankAble;
import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;
import static org.huangyalong.modules.system.configs.TenantConfigs.NAME_ENABLED;
import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;

public class SystemHelper extends FetchLoadHelper<String> {

    private static final long EXPIRE_MINUTES = 30;

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile SystemHelper instance;

    public static SystemHelper getInstance() {
        if (!initialized) {
            synchronized (SystemHelper.class) {
                if (!initialized) {
                    instance = new SystemHelper();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @Override
    protected long getExpireMinutes() {
        // 系统配置变更不频繁，30分钟可有效减少数据库查询
        return EXPIRE_MINUTES;
    }

    @Override
    protected String fetch(Serializable id) {
        if (ObjectUtil.isNotNull(id)) {
            return QueryChain.of(System.class)
                    .select(SYSTEM.CONFIGS)
                    .where(SYSTEM.ID.eq(id))
                    .oneAs(String.class);
        } else return null;
    }

    /**
     * 检查是否允许租户功能
     *
     * @return 是否允许
     */
    public static boolean allowTenant() {
        var enabled = ofBlankAble(getConfigs())
                .map(JSONUtil::parseObj)
                .orElseGet(JSONUtil::createObj)
                .getByPath(NAME_ENABLED, Boolean.class);
        return ofNullable(enabled)
                .orElse(Boolean.TRUE);
    }

    /**
     * 获取系统配置信息
     *
     * @return 配置信息
     */
    public static String getConfigs() {
        if (ObjectUtil.isNotNull(CONFIG_ID)) {
            var sId = (Serializable) CONFIG_ID;
            return getInstance().get(sId);
        } else return null;
    }

    /**
     * 加载系统配置信息到缓存
     */
    public static void load() {
        if (ObjectUtil.isNull(CONFIG_ID)) return;
        var sId = (Serializable) CONFIG_ID;
        getInstance().load(sId);
    }
}

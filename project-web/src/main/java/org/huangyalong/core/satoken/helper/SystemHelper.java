package org.huangyalong.core.satoken.helper;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.System;
import org.myframework.core.helper.FetchLoadHelper;

import java.io.Serializable;

import static cn.hutool.core.text.CharSequenceUtil.EMPTY;
import static org.huangyalong.core.constants.SystemConstants.CODE_TENANT;
import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;

/**
 * 系统配置缓存助手
 * <p>
 * 全量加载配置为域嵌套 JSONObject（与 /system/configs 的查询语义一致），本地缓存 30 分钟
 */
public class SystemHelper extends FetchLoadHelper<JSONObject> {

    private static final long EXPIRE_MINUTES = 30;

    private static final String CACHE_KEY = "all";

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

    /**
     * 全量加载配置：查询全部配置行并按 code 合并为域嵌套 JSON（缓存键不参与查询）
     */
    @Override
    protected JSONObject fetch(Serializable id) {
        if (ObjectUtil.isNotNull(id)) {
            var rows = QueryChain.of(System.class)
                    .where(SYSTEM.CODE.isNotNull())
                    .and(SYSTEM.CODE.ne(EMPTY))
                    .list();
            var configs = new JSONObject();
            for (var row : rows)
                configs.set(row.getCode(), row.getConfigs());
            return configs;
        } else return null;
    }

    /**
     * 检查是否允许租户功能
     *
     * @return 是否允许
     */
    public static boolean allowTenant() {
        var path = CODE_TENANT + ".enabled";
        var enabled = Opt.ofNullable(getConfigs())
                .orElseGet(JSONObject::new)
                .getByPath(path, Boolean.class);
        return Opt.ofNullable(enabled)
                .orElse(Boolean.TRUE);
    }

    /**
     * 获取系统配置信息（域嵌套 JSON）
     *
     * @return 配置信息
     */
    public static JSONObject getConfigs() {
        if (ObjectUtil.isNotNull(CACHE_KEY)) {
            var sId = (Serializable) CACHE_KEY;
            return getInstance().get(sId);
        } else return null;
    }

    /**
     * 加载系统配置信息到缓存
     */
    public static void load() {
        if (ObjectUtil.isNull(CACHE_KEY)) return;
        var sId = (Serializable) CACHE_KEY;
        getInstance().load(sId);
    }
}

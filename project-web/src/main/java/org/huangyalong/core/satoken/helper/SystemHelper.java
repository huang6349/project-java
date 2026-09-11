package org.huangyalong.core.satoken.helper;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.System;
import org.myframework.core.helper.FetchLoadHelper;

import java.io.Serializable;

import static cn.hutool.core.collection.CollStreamUtil.toMap;
import static cn.hutool.core.text.CharSequenceUtil.EMPTY;
import static cn.hutool.core.util.ObjectUtil.equal;
import static org.huangyalong.core.constants.SystemConstants.CODE_RULES;
import static org.huangyalong.core.constants.SystemConstants.CODE_TENANT;
import static org.huangyalong.modules.system.domain.table.SystemTableDef.SYSTEM;
import static org.huangyalong.modules.system.enums.ConfigRule.RO;

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
     * <p>
     * 所有域统一注入只读标记（值由规则表派生）：ro 规则为 true，其余（rw/fr 与未登记规则）为 false
     */
    @Override
    protected JSONObject fetch(Serializable id) {
        if (ObjectUtil.isNotNull(id)) {
            var rows = QueryChain.of(System.class)
                    .where(SYSTEM.CODE.isNotNull())
                    .and(SYSTEM.CODE.ne(EMPTY))
                    .list();
            var configs = toMap(rows, System::getCode, this::toDomain);
            return JSONUtil.parseObj(configs);
        } else return null;
    }

    /**
     * 配置行转域 JSON：统一注入只读标记（ro 规则为 true，其余为 false）
     */
    protected JSONObject toDomain(System row) {
        var domain = Opt.ofNullable(row)
                .map(System::getConfigs)
                .map(JSONUtil::parseObj)
                .orElseGet(JSONUtil::createObj);
        var readonly = Opt.ofNullable(row)
                .map(System::getCode)
                .map(CODE_RULES::get)
                .map(rule -> equal(RO, rule))
                .orElse(Boolean.FALSE);
        domain.set("readonly", readonly);
        return domain;
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

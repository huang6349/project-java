package org.huangyalong.core.satoken.helper;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.helper.FetchLoadHelper;

import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public class TenantHelper extends FetchLoadHelper<Long> {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile TenantHelper instance;

    public static TenantHelper getInstance() {
        if (!initialized) {
            synchronized (TenantHelper.class) {
                if (!initialized) {
                    instance = new TenantHelper();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @Override
    protected Long fetch(Dict dict) {
        var id = dict.getLong("id");
        if (ObjectUtil.isNotEmpty(id)) {
            return QueryChain.of(User.class)
                    .select(USER.TENANT_ID)
                    .where(USER.ID.eq(id))
                    .oneAs(Long.class);
        } else return null;
    }

    /**
     * 根据用户编号获取租户编号
     *
     * @param id 用户编号
     * @return 租户编号
     */
    public static Long getTenant(Object id) {
        var dict = Dict.create()
                .set("id", id);
        return getInstance().get(dict);
    }

    /**
     * 加载指定用户的租户信息到缓存
     *
     * @param id 用户编号
     */
    public static void load(Object id) {
        var dict = Dict.create()
                .set("id", id);
        getInstance().load(dict);
    }
}

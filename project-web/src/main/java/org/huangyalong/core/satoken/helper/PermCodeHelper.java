package org.huangyalong.core.satoken.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.Perm;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.helper.FetchLoadHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.collection.CollUtil.addAllIfNotContains;
import static cn.hutool.core.collection.CollUtil.addIfAbsent;
import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.core.constants.Constants.SUPER_ADMIN_ID;
import static org.huangyalong.core.satoken.helper.TenantHelper.getActualTenant;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public class PermCodeHelper extends FetchLoadHelper<List<String>> {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile PermCodeHelper instance;

    public static PermCodeHelper getInstance() {
        if (!initialized) {
            synchronized (PermCodeHelper.class) {
                if (!initialized) {
                    instance = new PermCodeHelper();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @Override
    protected List<String> fetch(Serializable id) {
        var perms = new ArrayList<String>();
        if (ObjectUtil.equal(SUPER_ADMIN_ID, id))
            addIfAbsent(perms, "*");
        var tenantId = getActualTenant(id);
        if (ObjectUtil.isNotEmpty(tenantId) &&
                ObjectUtil.isNotEmpty(id)) {
            var roles = RoleHelper.fetch(tenantId, id);
            var roleEffective = CollUtil.isNotEmpty(roles);
            return addAllIfNotContains(perms, QueryChain.of(Perm.class)
                    .select(PERM.CODE)
                    .leftJoin(PERM_ASSOC)
                    .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                    .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                            .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                    .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                    .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                    .and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                            .and(PERM_ASSOC.TENANT_ID.eq(tenantId))
                            .and(PERM_ASSOC.ASSOC_ID.eq(id)))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName(), roleEffective)
                            .and(PERM_ASSOC.ASSOC_ID.in(roles, roleEffective)))
                    .listAs(String.class));
        } else return perms;
    }

    /**
     * 根据用户编号获取权限编码
     *
     * @param id 用户编号
     * @return 权限编码列表
     */
    public static List<String> getPermCode(Object id) {
        if (ObjectUtil.isNotNull(id)) {
            var sId = (Serializable) id;
            return getInstance().get(sId);
        } else return new ArrayList<>();
    }

    /**
     * 加载指定用户的权限编码到缓存
     *
     * @param id 用户编号
     */
    public static void load(Object id) {
        if (ObjectUtil.isNull(id)) return;
        var sId = (Serializable) id;
        getInstance().load(sId);
    }
}

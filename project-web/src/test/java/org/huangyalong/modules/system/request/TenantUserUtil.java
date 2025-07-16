package org.huangyalong.modules.system.request;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.domain.Tenant;
import org.huangyalong.modules.system.domain.User;

import java.io.Serializable;

import static cn.hutool.core.collection.CollUtil.newArrayList;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.TenantTableDef.TENANT;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface TenantUserUtil {

    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_DESC = RandomUtil.randomString(12);

    static TenantUserBO createBO(JSONObject object) {
        var tenantId = Tenant.create()
                .orderBy(TENANT.ID, Boolean.FALSE)
                .oneOpt()
                .map(Tenant::getId)
                .orElse(null);
        var userId = User.create()
                .orderBy(USER.ID, Boolean.FALSE)
                .oneOpt()
                .map(User::getId)
                .orElse(null);
        var roleId = Role.create()
                .orderBy(ROLE.ID, Boolean.FALSE)
                .oneOpt()
                .map(Role::getId)
                .orElse(null);
        var userBO = new TenantUserBO();
        userBO.setId(object.getLong("id"));
        userBO.setTenantId(tenantId);
        userBO.setUserId(userId);
        userBO.setRoleIds(newArrayList(roleId));
        userBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return userBO;
    }

    static TenantUserBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static TenantUserBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }
}

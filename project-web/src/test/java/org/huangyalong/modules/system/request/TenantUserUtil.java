package org.huangyalong.modules.system.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.system.domain.TenantAssoc;

import java.io.Serializable;

import static cn.hutool.core.collection.CollUtil.newArrayList;
import static org.huangyalong.modules.system.domain.table.TenantAssocTableDef.TENANT_ASSOC;

public interface TenantUserUtil {

    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_DESC = RandomUtil.randomString(12);

    static TenantUserBO createBO(JSONObject object) {
        var userBO = new TenantUserBO();
        userBO.setId(object.getLong("id"));
        userBO.setTenantId(TenantUtil.getId());
        userBO.setUserId(UserUtil.getId());
        userBO.setRoleIds(newArrayList(RoleUtil.getId()));
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

    static TenantAssoc getEntity() {
        return TenantAssoc.create()
                .orderBy(TENANT_ASSOC.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        return Opt.ofNullable(getEntity())
                .map(TenantAssoc::getId)
                .get();
    }
}

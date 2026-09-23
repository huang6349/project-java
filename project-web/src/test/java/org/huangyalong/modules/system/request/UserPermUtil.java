package org.huangyalong.modules.system.request;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public interface UserPermUtil {

    static UserPermBO createBO() {
        var tenantId = TenantUtil.getId();
        var permId = PermUtil.getId();
        var id = UserUtil.getId();
        var permBO = new UserPermBO();
        permBO.setTenantId(tenantId);
        permBO.setPermIds(newArrayList(permId));
        permBO.setId(id);
        return permBO;
    }
}

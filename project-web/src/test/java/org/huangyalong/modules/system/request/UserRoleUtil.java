package org.huangyalong.modules.system.request;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public interface UserRoleUtil {

    static UserRoleBO createBO() {
        var roleId = RoleUtil.getId();
        var id = UserUtil.getId();
        var roleBO = new UserRoleBO();
        roleBO.setTenantId(TenantUtil.getId());
        roleBO.setRoleIds(newArrayList(roleId));
        roleBO.setId(id);
        return roleBO;
    }
}

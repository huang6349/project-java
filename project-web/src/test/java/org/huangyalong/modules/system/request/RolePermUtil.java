package org.huangyalong.modules.system.request;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public interface RolePermUtil {

    static RolePermBO createBO() {
        var permId = PermUtil.getId();
        var id = RoleUtil.getId();
        var permBO = new RolePermBO();
        permBO.setPermIds(newArrayList(permId));
        permBO.setId(id);
        return permBO;
    }
}

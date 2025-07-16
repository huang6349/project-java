package org.huangyalong.modules.system.request;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public interface RolePermUtil {

    static RolePermBO createBO() {
        var permBO = new RolePermBO();
        permBO.setPermIds(newArrayList(PermUtil.getId()));
        permBO.setId(RoleUtil.getId());
        return permBO;
    }
}

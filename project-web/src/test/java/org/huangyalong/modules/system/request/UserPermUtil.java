package org.huangyalong.modules.system.request;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public interface UserPermUtil {

    static UserPermBO createBO() {
        var permId = PermUtil.getId();
        var id = UserUtil.getId();
        var permBO = new UserPermBO();
        permBO.setPermIds(newArrayList(permId));
        permBO.setId(id);
        return permBO;
    }
}

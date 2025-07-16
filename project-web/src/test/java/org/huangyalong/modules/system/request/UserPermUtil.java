package org.huangyalong.modules.system.request;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public interface UserPermUtil {

    static UserPermBO createBO() {
        var permBO = new UserPermBO();
        permBO.setPermIds(newArrayList(PermUtil.getId()));
        permBO.setId(UserUtil.getId());
        return permBO;
    }
}

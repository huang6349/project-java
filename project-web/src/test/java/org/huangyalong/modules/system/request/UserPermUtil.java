package org.huangyalong.modules.system.request;

import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.domain.User;

import static cn.hutool.core.collection.CollUtil.newArrayList;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface UserPermUtil {

    static UserPermBO createBO() {
        var permId = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .oneOpt()
                .map(Perm::getId)
                .orElse(null);
        var id = User.create()
                .orderBy(USER.ID, Boolean.FALSE)
                .oneOpt()
                .map(User::getId)
                .orElse(null);
        var permBO = new UserPermBO();
        permBO.setPermIds(newArrayList(permId));
        permBO.setId(id);
        return permBO;
    }
}

package org.huangyalong.modules.system.request;

import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.domain.Role;

import static cn.hutool.core.collection.CollUtil.newArrayList;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;

public interface RolePermUtil {

    static RolePermBO createBO() {
        var permId = Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .oneOpt()
                .map(Perm::getId)
                .orElse(null);
        var id = Role.create()
                .orderBy(ROLE.ID, Boolean.FALSE)
                .oneOpt()
                .map(Role::getId)
                .orElse(null);
        var permBO = new RolePermBO();
        permBO.setPermIds(newArrayList(permId));
        permBO.setId(id);
        return permBO;
    }
}

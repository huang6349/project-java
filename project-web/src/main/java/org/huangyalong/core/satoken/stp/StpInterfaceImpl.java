package org.huangyalong.core.satoken.stp;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.service.UserPermService;
import org.huangyalong.modules.system.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StpInterfaceImpl implements StpInterface {

    private final UserPermService permService;

    private final UserRoleService roleService;

    @Override
    public List<String> getPermissionList(Object loginId,
                                          String loginType) {
        if (ObjectUtil.isNull(loginId))
            return ListUtil.empty();
        return permService.getBlockByUserId(loginId)
                .stream()
                .map(Perm::getCode)
                .toList();
    }

    @Override
    public List<String> getRoleList(Object loginId,
                                    String loginType) {
        if (ObjectUtil.isNull(loginId))
            return ListUtil.empty();
        return roleService.getBlockByUserId(loginId)
                .stream()
                .map(Role::getCode)
                .toList();
    }
}

package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.Getter;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.mapper.RoleMapper;
import org.huangyalong.modules.system.request.RoleAssocBO;
import org.huangyalong.modules.system.request.UserRoleBO;
import org.huangyalong.modules.system.service.RoleAssocService;
import org.huangyalong.modules.system.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@Getter
@Service
public class UserRoleServiceImpl extends ReactorServiceImpl<RoleMapper, Role> implements UserRoleService {

    @Autowired
    private RoleAssocService assocService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(UserRoleBO roleBO) {
        var tenantId = Opt.ofNullable(roleBO)
                .map(UserRoleBO::getTenantId)
                .get();
        var roleIds = Opt.ofNullable(roleBO)
                .map(UserRoleBO::getRoleIds)
                .get();
        var id = Opt.ofNullable(roleBO)
                .map(UserRoleBO::getId)
                .get();
        var assocBO = new RoleAssocBO();
        assocBO.setTenantId(tenantId);
        assocBO.setRoleIds(roleIds);
        assocBO.setAssoc(USER.getTableName());
        assocBO.setId(id);
        return getAssocService()
                .assoc(assocBO);
    }
}

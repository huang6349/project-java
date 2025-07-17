package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.huangyalong.modules.system.service.UserPermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AllArgsConstructor
@Service
public class UserPermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements UserPermService {

    private final PermAssocService assocService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(UserPermBO permBO) {
        var tenantId = Opt.ofNullable(permBO)
                .map(UserPermBO::getTenantId)
                .get();
        var permIds = Opt.ofNullable(permBO)
                .map(UserPermBO::getPermIds)
                .get();
        var id = Opt.ofNullable(permBO)
                .map(UserPermBO::getId)
                .get();
        var assocBO = new PermAssocBO();
        assocBO.setTenantId(tenantId);
        assocBO.setPermIds(permIds);
        assocBO.setAssoc(USER.getTableName());
        assocBO.setId(id);
        return assocService.assoc(assocBO);
    }
}

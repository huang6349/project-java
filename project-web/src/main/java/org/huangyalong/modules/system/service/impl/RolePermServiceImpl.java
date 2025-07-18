package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.RolePermBO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.huangyalong.modules.system.service.RolePermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;

@AllArgsConstructor
@Service
public class RolePermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements RolePermService {

    private final PermAssocService assocService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(RolePermBO permBO) {
        var permIds = Opt.ofNullable(permBO)
                .map(RolePermBO::getPermIds)
                .get();
        var id = Opt.ofNullable(permBO)
                .map(RolePermBO::getId)
                .get();
        var assocBO = new PermAssocBO();
        assocBO.setPermIds(permIds);
        assocBO.setAssoc(ROLE.getTableName());
        assocBO.setId(id);
        return assocService.assoc(assocBO);
    }
}

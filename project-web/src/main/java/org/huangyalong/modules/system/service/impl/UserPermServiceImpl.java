package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.request.PermAssocBO;
import org.huangyalong.modules.system.request.UserPermBO;
import org.huangyalong.modules.system.service.PermAssocService;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.table.PermAssocTableDef.PERM_ASSOC;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AllArgsConstructor
@Service
public class UserPermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements UserPermService {

    private final PermAssocService assocService;

    @Override
    public Mono<List<Perm>> getAllByUserId(Serializable id) {
        var roles = UserHelper.getRole(id);
        var query = QueryWrapper.create()
                .select(PERM.ALL_COLUMNS)
                .from(PERM)
                .leftJoin(PERM_ASSOC)
                .on(PERM_ASSOC.PERM_ID.eq(PERM.ID))
                .where(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(PERM_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(PERM_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(PERM_ASSOC.CATEGORY.eq(AssocCategory.TYPE0));
        if (CollUtil.isNotEmpty(roles))
            query.and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                    .and(PERM_ASSOC.ASSOC_ID.eq(id))
                    .or(PERM_ASSOC.ASSOC.eq(ROLE.getTableName())
                            .and(PERM_ASSOC.ASSOC_ID.in(roles))));
        else query.and(PERM_ASSOC.ASSOC.eq(USER.getTableName())
                .and(PERM_ASSOC.ASSOC_ID.eq(id)));
        return list(query).collectList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> assoc(UserPermBO permBO) {
        var permIds = Opt.ofNullable(permBO)
                .map(UserPermBO::getPermIds)
                .get();
        var id = Opt.ofNullable(permBO)
                .map(UserPermBO::getId)
                .get();
        var assocBO = new PermAssocBO();
        assocBO.setPermIds(permIds);
        assocBO.setAssoc(USER.getTableName());
        assocBO.setId(id);
        return assocService.assoc(assocBO);
    }
}

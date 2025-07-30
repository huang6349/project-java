package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.TenantUserBO;
import org.huangyalong.modules.system.request.TenantUserQueries;
import org.huangyalong.modules.system.response.TenantUserVO;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.modules.system.domain.UserExtras.*;
import static org.huangyalong.modules.system.domain.table.TenantAssocTableDef.TENANT_ASSOC;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface TenantUserService extends ReactorService<User> {

    default QueryWrapper getQueryWrapper(TenantUserQueries queries,
                                         QueryWrapper query) {
        query.where(TENANT_ASSOC.TENANT_ID.like(queries.getTenantId(), If::notNull));
        query.where(USER.USERNAME.like(queries.getUsername(), If::hasText));
        query.where(USER.MOBILE.like(queries.getMobile(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(TenantUserQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(TENANT_ASSOC.ID, Boolean.TRUE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(TENANT_ASSOC.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(TENANT_ASSOC.ID,
                        USER.USERNAME,
                        USER.MOBILE,
                        USER.EMAIL,
                        TENANT_ASSOC.DESC,
                        USER.LOGIN_TIME,
                        USER.STATUS,
                        TENANT_ASSOC.CREATE_TIME,
                        TENANT_ASSOC.UPDATE_TIME)
                .select(ue(USER.EXTRAS, NAME_NICKNAME).as(User::getNickname),
                        ue(USER.EXTRAS, NAME_AVATAR).as(User::getAvatar),
                        ue(USER.EXTRAS, NAME_GENDER).as(User::getGender),
                        ue(USER.EXTRAS, NAME_BIRTHDAY).as(User::getBirthday),
                        ue(USER.EXTRAS, NAME_ADDRESS).as(User::getAddress))
                .from(TENANT_ASSOC)
                .leftJoin(USER)
                .on(USER.ID.eq(TENANT_ASSOC.ASSOC_ID))
                .where(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(TENANT_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(TENANT_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(TENANT_ASSOC.ASSOC.eq(USER.getTableName()));
    }

    default Mono<TenantUserVO> getAssocById(Serializable id) {
        var query = QueryWrapper.create()
                .select(TENANT_ASSOC.ID,
                        TENANT_ASSOC.TENANT_ID,
                        TENANT_ASSOC.ASSOC_ID.as(TenantUserVO::getUserId),
                        TENANT_ASSOC.DESC,
                        TENANT_ASSOC.CREATE_TIME,
                        TENANT_ASSOC.UPDATE_TIME)
                .from(TENANT_ASSOC)
                .where(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0))
                .and(TENANT_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(TENANT_ASSOC.ASSOC.eq(USER.getTableName()))
                .and(TENANT_ASSOC.ID.eq(id));
        return getOneAs(query, TenantUserVO.class);
    }

    @Override
    default Mono<User> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(TenantUserBO userBO);

    Mono<Boolean> update(TenantUserBO userBO);

    Mono<Boolean> delete(Serializable id);
}

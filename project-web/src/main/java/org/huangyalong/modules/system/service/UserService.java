package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.request.UserBO;
import org.huangyalong.modules.system.request.UserQueries;
import org.myframework.base.response.OptionVO;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Optional;

import static org.huangyalong.modules.system.domain.UserExtras.*;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface UserService extends ReactorService<User> {

    default QueryWrapper getOptionWrapper(UserQueries queries) {
        var query = QueryWrapper.create()
                .select(USER.USERNAME.as(OptionVO::getLabel),
                        USER.ID.as(OptionVO::getValue))
                .from(USER);
        query.orderBy(USER.ID, Boolean.TRUE);
        return getQueryWrapper(queries, query);
    }

    default QueryWrapper getQueryWrapper(UserQueries queries,
                                         QueryWrapper query) {
        query.where(USER.USERNAME.like(queries.getUsername(), If::hasText));
        query.where(USER.MOBILE.like(queries.getMobile(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(UserQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(USER.ID, Boolean.TRUE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(USER.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(USER.ID,
                        USER.USERNAME,
                        USER.MOBILE,
                        USER.EMAIL,
                        USER.DESC,
                        USER.LOGIN_TIME,
                        USER.STATUS,
                        USER.TENANT_ID,
                        USER.CREATE_TIME,
                        USER.UPDATE_TIME)
                .select(ue(USER.EXTRAS, NAME_NICKNAME).as(User::getNickname),
                        ue(USER.EXTRAS, NAME_AVATAR).as(User::getAvatar),
                        ue(USER.EXTRAS, NAME_GENDER).as(User::getGender),
                        ue(USER.EXTRAS, NAME_BIRTHDAY).as(User::getBirthday),
                        ue(USER.EXTRAS, NAME_ADDRESS).as(User::getAddress))
                .from(USER);
    }

    default Optional<User> getBlockByIdOpt(Serializable id) {
        var query = getQueryWrapper(id);
        return getBlockService()
                .getOneOpt(query);
    }

    default Optional<User> getBlockByIdOpt(Object id) {
        var convert = (Serializable) id;
        return getBlockByIdOpt(convert);
    }

    @Override
    default Mono<User> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(UserBO userBO);

    Mono<Boolean> update(UserBO userBO);

    Mono<Boolean> delete(Serializable id);
}

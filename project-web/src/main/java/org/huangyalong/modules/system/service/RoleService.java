package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.request.RoleBO;
import org.huangyalong.modules.system.request.RoleQueries;
import org.myframework.base.response.OptionVO;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;

public interface RoleService extends ReactorService<Role> {

    default QueryWrapper getOptionWrapper(RoleQueries queries) {
        var query = QueryWrapper.create()
                .select(ROLE.NAME.as(OptionVO::getLabel),
                        ROLE.ID.as(OptionVO::getValue))
                .from(ROLE);
        query.orderBy(ROLE.ID, Boolean.TRUE);
        return getQueryWrapper(queries, query);
    }

    default QueryWrapper getQueryWrapper(RoleQueries queries,
                                         QueryWrapper query) {
        query.where(ROLE.NAME.like(queries.getName(), If::hasText));
        query.where(ROLE.CODE.like(queries.getCode(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(RoleQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(ROLE.ID, Boolean.TRUE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(ROLE.ID,
                        ROLE.NAME,
                        ROLE.CODE,
                        ROLE.DESC,
                        ROLE.STATUS,
                        ROLE.CREATE_TIME,
                        ROLE.UPDATE_TIME)
                .from(ROLE);
    }

    Mono<Boolean> add(RoleBO roleBO);

    Mono<Boolean> update(RoleBO roleBO);

    Mono<Boolean> delete(Serializable id);
}

package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.request.PermBO;
import org.huangyalong.modules.system.request.PermQueries;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;

public interface PermService extends ReactorService<Perm> {

    default QueryWrapper getQueryWrapper(PermQueries queries,
                                         QueryWrapper query) {
        query.where(PERM.NAME.like(queries.getName(), If::hasText));
        query.where(PERM.CODE.like(queries.getCode(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(PermQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(PERM.ID, Boolean.FALSE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(PERM.ID,
                        PERM.NAME,
                        PERM.CODE,
                        PERM.DESC,
                        PERM.STATUS,
                        PERM.CREATE_TIME,
                        PERM.UPDATE_TIME)
                .from(PERM);
    }

    Mono<Boolean> add(PermBO permBO);

    Mono<Boolean> update(PermBO permBO);

    Mono<Boolean> delete(Serializable id);
}

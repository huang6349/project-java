package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.system.domain.Tenant;
import org.huangyalong.modules.system.request.TenantBO;
import org.huangyalong.modules.system.request.TenantQueries;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.system.domain.TenantExtras.NAME_ABBR;
import static org.huangyalong.modules.system.domain.TenantExtras.NAME_AREA;
import static org.huangyalong.modules.system.domain.table.TenantTableDef.TENANT;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface TenantService extends ReactorService<Tenant> {

    default QueryWrapper getQueryWrapper(TenantQueries queries,
                                         QueryWrapper query) {
        query.where(TENANT.NAME.like(queries.getName(), If::hasText));
        query.where(TENANT.CODE.like(queries.getCode(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(TenantQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(TENANT.ID, Boolean.FALSE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(TENANT.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(TENANT.ID,
                        TENANT.NAME,
                        TENANT.CODE,
                        TENANT.CATEGORY,
                        TENANT.ADDRESS,
                        TENANT.DESC,
                        TENANT.STATUS,
                        TENANT.CREATE_TIME,
                        TENANT.UPDATE_TIME)
                .select(ue(TENANT.EXTRAS, NAME_ABBR).as(Tenant::getAbbr),
                        ue(TENANT.EXTRAS, NAME_AREA).as(Tenant::getArea))
                .from(TENANT);
    }

    @Override
    default Mono<Tenant> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(TenantBO tenantBO);

    Mono<Boolean> update(TenantBO tenantBO);

    Mono<Boolean> delete(Serializable id);
}

package org.huangyalong.modules.notify.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.request.AppBO;
import org.huangyalong.modules.notify.request.AppQueries;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.notify.domain.CategoryConfigs.NAME_FREQ;
import static org.huangyalong.modules.notify.domain.table.NotifyAppTableDef.NOTIFY_APP;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface NotifyAppService extends ReactorService<NotifyApp> {

    default QueryWrapper getQueryWrapper(AppQueries queries,
                                         QueryWrapper query) {
        query.where(NOTIFY_APP.CATEGORY_ID.eq(queries.getCategoryId(), If::notNull));
        query.where(NOTIFY_APP.NAME.like(queries.getName(), If::hasText));
        query.where(NOTIFY_APP.CODE.like(queries.getCode(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(AppQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(NOTIFY_APP.ID, Boolean.TRUE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(NOTIFY_APP.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(NOTIFY_APP.ID,
                        NOTIFY_APP.CATEGORY_ID,
                        NOTIFY_APP.NAME,
                        NOTIFY_APP.CODE,
                        NOTIFY_APP.DESC,
                        NOTIFY_APP.STATUS,
                        NOTIFY_APP.CREATE_TIME,
                        NOTIFY_APP.UPDATE_TIME)
                .select(ue(NOTIFY_APP.CONFIGS, NAME_FREQ).as(NotifyApp::getFreq))
                .from(NOTIFY_APP);
    }

    @Override
    default Mono<NotifyApp> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(AppBO appBO);

    Mono<Boolean> update(AppBO appBO);

    Mono<Boolean> delete(Serializable id);
}

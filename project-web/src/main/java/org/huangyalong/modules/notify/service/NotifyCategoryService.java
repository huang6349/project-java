package org.huangyalong.modules.notify.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.huangyalong.modules.notify.request.CategoryBO;
import org.huangyalong.modules.notify.request.CategoryQueries;
import org.myframework.base.response.OptionVO;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.notify.domain.CategoryConfigs.NAME_FREQ;
import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface NotifyCategoryService extends ReactorService<NotifyCategory> {

    default QueryWrapper getOptionWrapper(CategoryQueries queries) {
        var query = QueryWrapper.create()
                .select(NOTIFY_CATEGORY.NAME.as(OptionVO::getLabel),
                        NOTIFY_CATEGORY.CODE.as(OptionVO::getValue))
                .from(NOTIFY_CATEGORY);
        query.orderBy(NOTIFY_CATEGORY.ID, Boolean.TRUE);
        return getQueryWrapper(queries, query);
    }

    default QueryWrapper getQueryWrapper(CategoryQueries queries,
                                         QueryWrapper query) {
        query.where(NOTIFY_CATEGORY.NAME.like(queries.getName(), If::hasText));
        query.where(NOTIFY_CATEGORY.CODE.like(queries.getCode(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(CategoryQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(NOTIFY_CATEGORY.ID, Boolean.TRUE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(NOTIFY_CATEGORY.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(NOTIFY_CATEGORY.ID,
                        NOTIFY_CATEGORY.NAME,
                        NOTIFY_CATEGORY.CODE,
                        NOTIFY_CATEGORY.DESC,
                        NOTIFY_CATEGORY.STATUS,
                        NOTIFY_CATEGORY.CREATE_TIME,
                        NOTIFY_CATEGORY.UPDATE_TIME)
                .select(ue(NOTIFY_CATEGORY.CONFIGS, NAME_FREQ).as(NotifyCategory::getFreq))
                .from(NOTIFY_CATEGORY);
    }

    @Override
    default Mono<NotifyCategory> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(CategoryBO categoryBO);

    Mono<Boolean> update(CategoryBO categoryBO);

    Mono<Boolean> delete(Serializable id);
}

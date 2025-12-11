package org.huangyalong.modules.webhook.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.webhook.domain.Webhook;
import org.huangyalong.modules.webhook.request.WebhookBO;
import org.huangyalong.modules.webhook.request.WebhookQueries;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.webhook.domain.WebhookExtras.*;
import static org.huangyalong.modules.webhook.domain.table.WebhookTableDef.WEBHOOK;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public interface WebhookService extends ReactorService<Webhook> {

    default QueryWrapper getQueryWrapper(WebhookQueries queries,
                                         QueryWrapper query) {
        query.where(WEBHOOK.URL.like(queries.getUrl(), If::hasText));
        query.where(ue(WEBHOOK.EXTRAS, NAME_FORMAT).eq(queries.getFormat(), If::hasText));
        query.where(ue(WEBHOOK.EXTRAS, NAME_TRIGGER).eq(queries.getTrigger(), If::hasText));
        query.where(WEBHOOK.DESC.like(queries.getDesc(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(WebhookQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(WEBHOOK.ID, Boolean.FALSE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where(WEBHOOK.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select(WEBHOOK.ID,
                        WEBHOOK.URL,
                        WEBHOOK.DESC,
                        WEBHOOK.STATUS,
                        WEBHOOK.CREATE_TIME,
                        WEBHOOK.UPDATE_TIME)
                .select(ue(WEBHOOK.EXTRAS, NAME_FORMAT).as(Webhook::getFormat),
                        ue(WEBHOOK.EXTRAS, NAME_SECRET).as(Webhook::getSecret),
                        ue(WEBHOOK.EXTRAS, NAME_TRIGGER).as(Webhook::getTrigger))
                .from(WEBHOOK);
    }

    @Override
    default Mono<Webhook> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add(WebhookBO webhookBO);

    Mono<Boolean> update(WebhookBO webhookBO);

    Mono<Boolean> test(Serializable id);

    Mono<Boolean> delete(Serializable id);
}

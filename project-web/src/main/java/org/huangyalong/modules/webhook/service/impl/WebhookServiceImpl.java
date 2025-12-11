package org.huangyalong.modules.webhook.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.Getter;
import org.huangyalong.modules.webhook.domain.Webhook;
import org.huangyalong.modules.webhook.mapper.WebhookMapper;
import org.huangyalong.modules.webhook.request.WebhookBO;
import org.huangyalong.modules.webhook.service.WebhookService;
import org.myframework.core.exception.BusinessException;
import org.myframework.webhook.WebhookRequest;
import org.myframework.webhook.helper.WebhookHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static org.huangyalong.modules.webhook.domain.table.WebhookTableDef.WEBHOOK;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@Getter
@Service
public class WebhookServiceImpl extends ReactorServiceImpl<WebhookMapper, Webhook> implements WebhookService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(WebhookBO webhookBO) {
        validateAddOrUpdate(webhookBO);
        var data = Webhook.create()
                .with(webhookBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(WebhookBO webhookBO) {
        validateAddOrUpdate(webhookBO);
        var id = Opt.ofNullable(webhookBO)
                .map(WebhookBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(webhookBO);
        return updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> test(Serializable id) {
        var query = getQueryWrapper(id);
        var data = getBlockService()
                .getOneOpt(query)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        var request = WebhookRequest.builder()
                .url(data.getUrl())
                .eventType("test")
                .data(JSONUtil.createObj())
                .format(data.getFormat())
                .secret(data.getSecret())
                .timeout(10000)
                .build();
        var response = WebhookHelper.sendWebhook(request);
        if (BooleanUtil.isFalse(response.getSuccess()))
            throw new BusinessException(response.getMessage());
        return Mono.just(Boolean.TRUE);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data);
    }

    void validateAddOrUpdate(WebhookBO webhookBO) {
        var url = Opt.ofNullable(webhookBO)
                .map(WebhookBO::getUrl)
                .get();
        if (StrUtil.isBlank(url)) return;
        var id = Opt.ofNullable(webhookBO)
                .map(WebhookBO::getId)
                .get();
        var exists = queryChain()
                .where(WEBHOOK.URL.eq(url))
                .and(WEBHOOK.URL.isNotNull())
                .and(WEBHOOK.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("推送地址已存在");
    }
}

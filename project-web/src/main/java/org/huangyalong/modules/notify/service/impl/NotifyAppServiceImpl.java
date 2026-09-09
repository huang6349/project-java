package org.huangyalong.modules.notify.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.huangyalong.modules.notify.mapper.NotifyAppMapper;
import org.huangyalong.modules.notify.request.AppBO;
import org.huangyalong.modules.notify.service.NotifyAppService;
import org.myframework.core.exception.BusinessException;
import org.myframework.extra.eventbus.BusHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.huangyalong.extra.notify.helper.NotifyHelper.getFreq;
import static org.huangyalong.modules.notify.domain.table.NotifyAppTableDef.NOTIFY_APP;
import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.constants.Subscribe.NOTIFY_FREQ_LISTENER;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;
import static org.myframework.core.util.ServiceUtil.randomCode;

@Service
public class NotifyAppServiceImpl extends ReactorServiceImpl<NotifyAppMapper, NotifyApp> implements NotifyAppService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(AppBO appBO) {
        validateAddOrUpdate(appBO);
        var code = Opt.ofNullable(appBO)
                .map(this::generateCode)
                .get();
        var data = NotifyApp.create()
                .setCode(code)
                .with(appBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(AppBO appBO) {
        validateAddOrUpdate(appBO);
        updateFreq(appBO);
        var id = Opt.ofNullable(appBO)
                .map(AppBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(appBO);
        return updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data);
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }

    void validateAddOrUpdate(AppBO payload) {
        validateCategoryNotModified(payload);
        validateCategoryExists(payload);
    }

    void validateCategoryNotModified(AppBO payload) {
        var categoryId = Opt.ofNullable(payload)
                .map(AppBO::getCategoryId)
                .get();
        var id = Opt.ofNullable(payload)
                .map(AppBO::getId)
                .get();
        if (ObjectUtil.isNull(id)) return;
        var exists = queryChain()
                .where(NOTIFY_APP.CATEGORY_ID.ne(categoryId))
                .and(NOTIFY_APP.CATEGORY_ID.isNotNull())
                .and(NOTIFY_APP.ID.eq(id))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("所属类别不允许修改");
    }

    void validateCategoryExists(AppBO payload) {
        var categoryId = Opt.ofNullable(payload)
                .map(AppBO::getCategoryId)
                .get();
        var id = Opt.ofNullable(payload)
                .map(AppBO::getId)
                .get();
        if (ObjectUtil.isNotNull(id)) return;
        var exists = NotifyCategory.create()
                .where(NOTIFY_CATEGORY.ID.eq(categoryId))
                .exists();
        if (BooleanUtil.isTrue(exists)) return;
        throw new BusinessException("所属类别不存在");
    }

    String generateCode(AppBO appBO) {
        var categoryId = Opt.ofNullable(appBO)
                .map(AppBO::getCategoryId)
                .get();
        var categoryCode = NotifyCategory.create()
                .select(NOTIFY_CATEGORY.CODE)
                .where(NOTIFY_CATEGORY.ID.eq(categoryId))
                .oneAs(String.class);
        if (StrUtil.isNotBlank(categoryCode)) {
            return format("{}-{}", categoryCode, randomCode());
        } else throw new BusinessException("类别代码不存在");
    }

    void updateFreq(AppBO appBO) {
        var freq = Opt.ofNullable(appBO)
                .map(AppBO::getFreq)
                .get();
        var id = Opt.ofNullable(appBO)
                .map(AppBO::getId)
                .get();
        if (ObjectUtil.isNull(id)) return;
        var code = queryChain()
                .select(NOTIFY_APP.CODE)
                .where(NOTIFY_APP.ID.eq(id))
                .oneAs(String.class);
        if (ObjectUtil.isNull(code)) return;
        if (ObjectUtil.equal(freq, getFreq(code))) return;
        BusHelper.send(NOTIFY_FREQ_LISTENER, code);
    }
}

package org.huangyalong.modules.notify.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.huangyalong.modules.notify.mapper.NotifyCategoryMapper;
import org.huangyalong.modules.notify.request.CategoryBO;
import org.huangyalong.modules.notify.service.NotifyCategoryService;
import org.myframework.extra.eventbus.BusHelper;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;
import static org.huangyalong.modules.notify.helper.NotifyHelper.getFreq;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.constants.Subscribe.NOTIFY_FREQ_LISTENER;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@Service
public class NotifyCategoryServiceImpl extends ReactorServiceImpl<NotifyCategoryMapper, NotifyCategory> implements NotifyCategoryService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(CategoryBO categoryBO) {
        validateAddOrUpdate(categoryBO);
        var data = NotifyCategory.create()
                .with(categoryBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(CategoryBO categoryBO) {
        validateAddOrUpdate(categoryBO);
        updateFreq(categoryBO);
        var id = Opt.ofNullable(categoryBO)
                .map(CategoryBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(categoryBO);
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

    void validateAddOrUpdate(CategoryBO payload) {
        validateNameUnique(payload);
        validateCodeUnique(payload);
    }

    void validateNameUnique(CategoryBO payload) {
        var name = Opt.ofNullable(payload)
                .map(CategoryBO::getName)
                .get();
        var id = Opt.ofNullable(payload)
                .map(CategoryBO::getId)
                .get();
        var exists = queryChain()
                .where(NOTIFY_CATEGORY.NAME.eq(name))
                .and(NOTIFY_CATEGORY.NAME.isNotNull())
                .and(NOTIFY_CATEGORY.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("名称已存在");
    }

    void validateCodeUnique(CategoryBO payload) {
        var code = Opt.ofNullable(payload)
                .map(CategoryBO::getCode)
                .get();
        var id = Opt.ofNullable(payload)
                .map(CategoryBO::getId)
                .get();
        var exists = queryChain()
                .where(NOTIFY_CATEGORY.CODE.eq(code))
                .and(NOTIFY_CATEGORY.CODE.isNotNull())
                .and(NOTIFY_CATEGORY.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("代码已存在");
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }

    void updateFreq(CategoryBO categoryBO) {
        var freq = Opt.ofNullable(categoryBO)
                .map(CategoryBO::getFreq)
                .get();
        var code = Opt.ofNullable(categoryBO)
                .map(CategoryBO::getCode)
                .get();
        var id = Opt.ofNullable(categoryBO)
                .map(CategoryBO::getId)
                .get();
        if (ObjectUtil.isNull(code)) return;
        if (ObjectUtil.isNull(id)) return;
        if (ObjectUtil.equal(freq, getFreq(code))) return;
        BusHelper.send(NOTIFY_FREQ_LISTENER, code);
    }
}

package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.request.PermBO;
import org.huangyalong.modules.system.service.PermService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@AllArgsConstructor
@Service
public class PermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements PermService {

    @Override
    public Mono<Boolean> add(PermBO permBO) {
        validateAddOrUpdate(permBO);
        var data = Perm.create()
                .with(permBO);
        return save(data);
    }

    @Override
    public Mono<Boolean> update(PermBO permBO) {
        validateAddOrUpdate(permBO);
        var id = Opt.ofNullable(permBO)
                .map(PermBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(permBO);
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

    void validateAddOrUpdate(PermBO permBO) {
        validateNameUnique(permBO);
        validateCodeUnique(permBO);
    }

    void validateNameUnique(PermBO permBO) {
        var name = Opt.ofNullable(permBO)
                .map(PermBO::getName)
                .get();
        var id = Opt.ofNullable(permBO)
                .map(PermBO::getId)
                .get();
        var exists = queryChain()
                .where(PERM.NAME.eq(name))
                .and(PERM.NAME.isNotNull())
                .and(PERM.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("名称已存在");
    }

    void validateCodeUnique(PermBO permBO) {
        var code = Opt.ofNullable(permBO)
                .map(PermBO::getCode)
                .get();
        var id = Opt.ofNullable(permBO)
                .map(PermBO::getId)
                .get();
        var exists = queryChain()
                .where(PERM.CODE.eq(code))
                .and(PERM.CODE.isNotNull())
                .and(PERM.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("代码已存在");
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }
}

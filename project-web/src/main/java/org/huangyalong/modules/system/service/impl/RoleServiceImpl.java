package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.mapper.RoleMapper;
import org.huangyalong.modules.system.request.RoleBO;
import org.huangyalong.modules.system.service.RoleService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@AllArgsConstructor
@Service
public class RoleServiceImpl extends ReactorServiceImpl<RoleMapper, Role> implements RoleService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(RoleBO roleBO) {
        validateAddOrUpdate(roleBO);
        var data = Role.create()
                .with(roleBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(RoleBO roleBO) {
        validateAddOrUpdate(roleBO);
        var id = Opt.ofNullable(roleBO)
                .map(RoleBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(roleBO);
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

    void validateAddOrUpdate(RoleBO roleBO) {
        validateNameUnique(roleBO);
        validateCodeUnique(roleBO);
    }

    void validateNameUnique(RoleBO roleBO) {
        var name = Opt.ofNullable(roleBO)
                .map(RoleBO::getName)
                .get();
        var id = Opt.ofNullable(roleBO)
                .map(RoleBO::getId)
                .get();
        var exists = queryChain()
                .where(ROLE.NAME.eq(name))
                .and(ROLE.NAME.isNotNull())
                .and(ROLE.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("名称已存在");
    }

    void validateCodeUnique(RoleBO roleBO) {
        var code = Opt.ofNullable(roleBO)
                .map(RoleBO::getCode)
                .get();
        var id = Opt.ofNullable(roleBO)
                .map(RoleBO::getId)
                .get();
        var exists = queryChain()
                .where(ROLE.CODE.eq(code))
                .and(ROLE.CODE.isNotNull())
                .and(ROLE.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("代码已存在");
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }
}

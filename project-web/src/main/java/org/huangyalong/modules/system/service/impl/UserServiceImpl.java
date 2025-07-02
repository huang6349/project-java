package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.AllArgsConstructor;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.request.UserBO;
import org.huangyalong.modules.system.service.UserService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@AllArgsConstructor
@Service
public class UserServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(UserBO userBO) {
        validateAddOrUpdate(userBO);
        var data = User.create()
                .with(userBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(UserBO userBO) {
        validateAddOrUpdate(userBO);
        var id = Opt.ofNullable(userBO)
                .map(UserBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(userBO);
        return updateById(data)
                .thenReturn(id)
                .doOnNext(UserHelper::load)
                .thenReturn(Boolean.TRUE);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data);
    }

    void validateAddOrUpdate(UserBO userBO) {
        validatePasswordEqual(userBO);
        validateUsernameUnique(userBO);
        validateUsernameUpdate(userBO);
        validateMobileUnique(userBO);
        validateEmailUnique(userBO);
    }

    void validatePasswordEqual(UserBO userBO) {
        var password1 = Opt.ofNullable(userBO)
                .map(UserBO::getPassword1)
                .orElse(null);
        var password2 = Opt.ofNullable(userBO)
                .map(UserBO::getPassword2)
                .orElse(null);
        var equal = ObjectUtil.equal(password1, password2);
        if (BooleanUtil.isTrue(equal)) return;
        throw new BusinessException("两次密码不一致");
    }

    void validateUsernameUnique(UserBO userBO) {
        var username = Opt.ofNullable(userBO)
                .map(UserBO::getUsername)
                .orElse(null);
        var id = Opt.ofNullable(userBO)
                .map(UserBO::getId)
                .orElse(null);
        if (ObjectUtil.isNotNull(id)) return;
        var exists = queryChain()
                .where(USER.USERNAME.eq(username))
                .and(USER.USERNAME.isNotNull())
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("帐号已存在");
    }

    void validateUsernameUpdate(UserBO userBO) {
        var username = Opt.ofNullable(userBO)
                .map(UserBO::getUsername)
                .orElse(null);
        var id = Opt.ofNullable(userBO)
                .map(UserBO::getId)
                .orElse(null);
        if (ObjectUtil.isNull(id)) return;
        var exists = queryChain()
                .where(USER.USERNAME.ne(username))
                .and(USER.USERNAME.isNotNull())
                .and(USER.ID.eq(id))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("帐号不允许修改");
    }

    void validateMobileUnique(UserBO userBO) {
        var mobile = Opt.ofNullable(userBO)
                .map(UserBO::getMobile)
                .orElse(null);
        var id = Opt.ofNullable(userBO)
                .map(UserBO::getId)
                .orElse(null);
        var exists = queryChain()
                .where(USER.MOBILE.eq(mobile))
                .and(USER.MOBILE.isNotNull())
                .and(USER.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("手机号码已存在");
    }

    void validateEmailUnique(UserBO userBO) {
        var email = Opt.ofNullable(userBO)
                .map(UserBO::getEmail)
                .orElse(null);
        var id = Opt.ofNullable(userBO)
                .map(UserBO::getId)
                .orElse(null);
        var exists = queryChain()
                .where(USER.EMAIL.eq(email))
                .and(USER.EMAIL.isNotNull())
                .and(USER.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("邮箱已存在");
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }
}

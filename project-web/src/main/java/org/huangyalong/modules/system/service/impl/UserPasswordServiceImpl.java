package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.request.UserPasswordBO;
import org.huangyalong.modules.system.service.UserPasswordService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@AllArgsConstructor
@Service
public class UserPasswordServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserPasswordService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(UserPasswordBO userPasswordBO) {
        var oldPassword = Opt.ofNullable(userPasswordBO)
                .map(UserPasswordBO::getOldPassword)
                .get();
        var newPassword = Opt.ofNullable(userPasswordBO)
                .map(UserPasswordBO::getNewPassword)
                .get();
        var confirm = Opt.ofNullable(userPasswordBO)
                .map(UserPasswordBO::getConfirm)
                .get();
        if (ObjectUtil.notEqual(newPassword, confirm))
            throw new BusinessException("两次密码不一致");
        var loginId = UserHelper.getLoginIdAsLong();
        var user = getBlockService()
                .getByIdOpt(loginId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        if (ObjectUtil.isNotEmpty(user.getPassword()))
            if (!BCrypt.checkpw(oldPassword, user.getPassword()))
                throw new BusinessException("旧的密码不正确");
        user.setSalt(Opt.ofNullable(user)
                .map(User::getSalt)
                .orElse(BCrypt.gensalt()));
        user.setPassword(BCrypt.hashpw(newPassword, user.getSalt()));
        return updateById(user);
    }
}

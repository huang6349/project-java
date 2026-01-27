package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import lombok.Getter;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.request.LoginBO;
import org.huangyalong.modules.system.response.JWTToken;
import org.huangyalong.modules.system.service.UserJWTService;
import org.myframework.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@Getter
@Service
public class UserJWTServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserJWTService {

    @Resource
    private CaptchaService captchaService;

    @Value("${app.captcha.enabled:true}")
    private Boolean captchaEnabled;

    @Transactional(rollbackFor = Exception.class)
    public Mono<JWTToken> authorize(LoginBO loginBO) {
        validateCaptcha(loginBO);
        var username = ofNullable(loginBO)
                .map(LoginBO::getUsername)
                .get();
        var password = ofNullable(loginBO)
                .map(LoginBO::getPassword)
                .get();
        var query = getBlockService()
                .query()
                .where(USER.USERNAME.eq(username, If::hasText))
                .or(USER.MOBILE.eq(username, If::hasText))
                .or(USER.EMAIL.eq(username, If::hasText));
        var data = getBlockService()
                .getOneOpt(query)
                .orElseThrow(() -> new BusinessException("帐号或密码错误"));
        if (!BCrypt.checkpw(password, data.getPassword()))
            throw new BusinessException("帐号或密码错误");
        return Mono.just(data)
                .map(User::getId)
                .map(JWTToken::create);
    }

    void validateCaptcha(LoginBO loginBO) {
        if (BooleanUtil.isFalse(getCaptchaEnabled())) return;
        var captchaVerification = ofNullable(loginBO)
                .map(LoginBO::getVerifyToken)
                .get();
        var captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        var rep = getCaptchaService().verification(captchaVO);
        var isSuccess = ofNullable(rep)
                .map(ResponseModel::isSuccess)
                .get();
        var repMsg = ofNullable(rep)
                .map(ResponseModel::getRepMsg)
                .get();
        if (BooleanUtil.isTrue(isSuccess)) return;
        throw new BusinessException(repMsg);
    }
}

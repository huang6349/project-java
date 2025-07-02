package org.huangyalong.core.satoken.stp;

import cn.dev33.satoken.listener.SaTokenListenerForSimple;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import lombok.AllArgsConstructor;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StpListener extends SaTokenListenerForSimple {

    @Override
    public void doLogin(String loginType,
                        Object loginId,
                        String tokenValue,
                        SaLoginParameter loginParameter) {
        UserHelper.send(loginId);
    }
}

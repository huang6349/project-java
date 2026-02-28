package org.huangyalong.modules.system.request;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import static org.huangyalong.core.constants.UserConstants.DEFAULT_PASSWORD;

public interface LoginUtil {

    String DEFAULT_USERNAME = UserUtil.DEFAULT_USERNAME;

    static LoginBO createBO(JSONObject object) {
        var loginBO = new LoginBO();
        loginBO.setVerifyToken(RandomUtil.randomString(12));
        loginBO.setUsername(object.getStr("username", DEFAULT_USERNAME));
        loginBO.setPassword(object.getStr("password", DEFAULT_PASSWORD));
        return loginBO;
    }

    static LoginBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }
}

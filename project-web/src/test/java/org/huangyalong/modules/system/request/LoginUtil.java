package org.huangyalong.modules.system.request;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import static org.huangyalong.modules.system.request.UserUtil.DEFAULT_PASSWORD;
import static org.huangyalong.modules.system.request.UserUtil.DEFAULT_USERNAME;

public interface LoginUtil {

    static LoginBO createBO(JSONObject object) {
        var loginBO = new LoginBO();
        loginBO.setUsername(object.getStr("username", DEFAULT_USERNAME));
        loginBO.setPassword(object.getStr("password", DEFAULT_PASSWORD));
        return loginBO;
    }

    static LoginBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }
}

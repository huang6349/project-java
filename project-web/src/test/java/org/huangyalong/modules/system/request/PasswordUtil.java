package org.huangyalong.modules.system.request;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import static org.huangyalong.core.constants.UserConstants.DEFAULT_PASSWORD;

public interface PasswordUtil {

    String DEFAULT_OLD_PASSWORD = DEFAULT_PASSWORD;
    String DEFAULT_NEW_PASSWORD = "a123456";
    String DEFAULT_CONFIRM = "a123456";

    static PasswordBO createBO(JSONObject object) {
        var passwordBO = new PasswordBO();
        passwordBO.setOldPassword(object.getStr("oldPassword", DEFAULT_OLD_PASSWORD));
        passwordBO.setNewPassword(object.getStr("newPassword", DEFAULT_NEW_PASSWORD));
        passwordBO.setConfirm(object.getStr("confirm", DEFAULT_CONFIRM));
        return passwordBO;
    }

    static PasswordBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }
}

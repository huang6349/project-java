package org.huangyalong.modules.system.request;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public interface PasswordUtil {

    String DEFAULT_OLD_PASSWORD = UserUtil.DEFAULT_PASSWORD;
    String DEFAULT_NEW_PASSWORD = UserUtil.UPDATED_PASSWORD;
    String DEFAULT_CONFIRM = UserUtil.UPDATED_PASSWORD;

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

package org.huangyalong.modules.system.request;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public interface UserPasswordUtil {

    String DEFAULT_OLD_PASSWORD = UserUtil.DEFAULT_PASSWORD;
    String DEFAULT_NEW_PASSWORD = UserUtil.UPDATED_PASSWORD;
    String DEFAULT_CONFIRM = UserUtil.UPDATED_PASSWORD;

    static UserPasswordBO createBO(JSONObject object) {
        var userPasswordBO = new UserPasswordBO();
        userPasswordBO.setOldPassword(object.getStr("oldPassword", DEFAULT_OLD_PASSWORD));
        userPasswordBO.setNewPassword(object.getStr("newPassword", DEFAULT_NEW_PASSWORD));
        userPasswordBO.setConfirm(object.getStr("confirm", DEFAULT_CONFIRM));
        return userPasswordBO;
    }

    static UserPasswordBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }
}

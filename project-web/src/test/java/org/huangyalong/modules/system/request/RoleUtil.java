package org.huangyalong.modules.system.request;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.Serializable;

public interface RoleUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);
    String DEFAULT_CODE = RandomUtil.randomString(12);
    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);
    String UPDATED_CODE = RandomUtil.randomString(12);
    String UPDATED_DESC = RandomUtil.randomString(12);

    static RoleBO createBO(JSONObject object) {
        var roleBO = new RoleBO();
        roleBO.setId(object.getLong("id"));
        roleBO.setName(object.getStr("name", DEFAULT_NAME));
        roleBO.setCode(object.getStr("code", DEFAULT_CODE));
        roleBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return roleBO;
    }

    static RoleBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("code", UPDATED_CODE)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static RoleBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }
}

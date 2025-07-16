package org.huangyalong.modules.system.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.system.domain.Perm;

import java.io.Serializable;

import static org.huangyalong.modules.system.domain.table.PermTableDef.PERM;

public interface PermUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);
    String DEFAULT_CODE = RandomUtil.randomString(12);
    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);
    String UPDATED_CODE = RandomUtil.randomString(12);
    String UPDATED_DESC = RandomUtil.randomString(12);

    static PermBO createBO(JSONObject object) {
        var permBO = new PermBO();
        permBO.setId(object.getLong("id"));
        permBO.setName(object.getStr("name", DEFAULT_NAME));
        permBO.setCode(object.getStr("code", DEFAULT_CODE));
        permBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return permBO;
    }

    static PermBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("code", UPDATED_CODE)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static PermBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static Perm getEntity() {
        return Perm.create()
                .orderBy(PERM.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(Perm::getId)
                .get();
    }
}

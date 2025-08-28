package org.huangyalong.modules.system.request;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.enums.UserGender;

import java.io.Serializable;

import static cn.hutool.core.util.RandomUtil.BASE_CHAR;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface UserUtil {

    String DEFAULT_USERNAME = RandomUtil.randomString(BASE_CHAR, 12);
    String DEFAULT_PASSWORD = "a123456";
    String DEFAULT_NICKNAME = RandomUtil.randomString(12);
    String DEFAULT_MOBILE = "13123456789";
    String DEFAULT_EMAIL = "13123456789@qq.com";
    String DEFAULT_GENDER = UserGender.TYPE0.getValue();
    String DEFAULT_ADDRESS = RandomUtil.randomString(12);
    String DEFAULT_DESC = RandomUtil.randomString(12);
    String DEFAULT_LABEL = StrUtil.format("{}（{}）", DEFAULT_USERNAME, DEFAULT_NICKNAME);

    String UPDATED_PASSWORD = "b123456";
    String UPDATED_NICKNAME = RandomUtil.randomString(12);
    String UPDATED_MOBILE = "15123456789";
    String UPDATED_EMAIL = "15123456789@qq.com";
    String UPDATED_GENDER = UserGender.TYPE1.getValue();
    String UPDATED_ADDRESS = RandomUtil.randomString(12);
    String UPDATED_DESC = RandomUtil.randomString(12);

    static UserBO createBO(JSONObject object) {
        var userBO = new UserBO();
        userBO.setId(object.getLong("id"));
        userBO.setUsername(object.getStr("username", DEFAULT_USERNAME));
        userBO.setPassword1(object.getStr("password", DEFAULT_PASSWORD));
        userBO.setPassword2(object.getStr("password", DEFAULT_PASSWORD));
        userBO.setNickname(object.getStr("nickname", DEFAULT_NICKNAME));
        userBO.setMobile(object.getStr("mobile", DEFAULT_MOBILE));
        userBO.setEmail(object.getStr("email", DEFAULT_EMAIL));
        userBO.setGender(object.getStr("gender", DEFAULT_GENDER));
        userBO.setAddress(object.getStr("address", DEFAULT_ADDRESS));
        userBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return userBO;
    }

    static UserBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("password", UPDATED_PASSWORD)
                .set("nickname", UPDATED_NICKNAME)
                .set("mobile", UPDATED_MOBILE)
                .set("email", UPDATED_EMAIL)
                .set("gender", UPDATED_GENDER)
                .set("address", UPDATED_ADDRESS)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static UserBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static User getEntity() {
        return User.create()
                .orderBy(USER.ID, Boolean.FALSE)
                .one();
    }

    static String getIdAsString() {
        var id = getId();
        return Opt.ofNullable(id)
                .map(Convert::toStr)
                .get();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(User::getId)
                .get();
    }
}

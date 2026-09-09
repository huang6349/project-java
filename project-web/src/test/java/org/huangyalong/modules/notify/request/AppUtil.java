package org.huangyalong.modules.notify.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.enums.NotifyFreq;

import java.io.Serializable;

import static org.huangyalong.modules.notify.domain.table.NotifyAppTableDef.NOTIFY_APP;

public interface AppUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);
    Integer DEFAULT_FREQ = NotifyFreq.TYPE0.getValue();
    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);
    Integer UPDATED_FREQ = NotifyFreq.TYPE1.getValue();
    String UPDATED_DESC = RandomUtil.randomString(12);

    static AppBO createBO(JSONObject object) {
        var appBO = new AppBO();
        appBO.setId(object.getLong("id"));
        appBO.setCategoryId(CategoryUtil.getId());
        appBO.setName(object.getStr("name", DEFAULT_NAME));
        appBO.setFreq(object.getInt("freq", DEFAULT_FREQ));
        appBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return appBO;
    }

    static AppBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("freq", UPDATED_FREQ)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static AppBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static NotifyApp getEntity() {
        return NotifyApp.create()
                .orderBy(NOTIFY_APP.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(NotifyApp::getId)
                .get();
    }
}

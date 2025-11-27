package org.huangyalong.modules.notify.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.huangyalong.modules.notify.enums.NotifyFreq;

import java.io.Serializable;

import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;

public interface CategoryUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);
    String DEFAULT_CODE = RandomUtil.randomString(12);
    Integer DEFAULT_FREQ = NotifyFreq.TYPE0.getValue();
    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);
    String UPDATED_CODE = RandomUtil.randomString(12);
    Integer UPDATED_FREQ = NotifyFreq.TYPE1.getValue();
    String UPDATED_DESC = RandomUtil.randomString(12);

    static CategoryBO createBO(JSONObject object) {
        var categoryBO = new CategoryBO();
        categoryBO.setId(object.getLong("id"));
        categoryBO.setName(object.getStr("name", DEFAULT_NAME));
        categoryBO.setCode(object.getStr("code", DEFAULT_CODE));
        categoryBO.setFreq(object.getInt("freq", DEFAULT_FREQ));
        categoryBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return categoryBO;
    }

    static CategoryBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("code", UPDATED_CODE)
                .set("freq", UPDATED_FREQ)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static CategoryBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static NotifyCategory getEntity() {
        return NotifyCategory.create()
                .orderBy(NOTIFY_CATEGORY.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(NotifyCategory::getId)
                .get();
    }
}

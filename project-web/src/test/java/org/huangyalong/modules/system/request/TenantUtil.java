package org.huangyalong.modules.system.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.system.domain.Tenant;
import org.huangyalong.modules.system.enums.TenantCategory;

import java.io.Serializable;

import static org.huangyalong.modules.system.domain.table.TenantTableDef.TENANT;

public interface TenantUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);
    String DEFAULT_ABBR = RandomUtil.randomString(8);
    String DEFAULT_AREA = RandomUtil.randomString(12);
    String DEFAULT_CATEGORY = TenantCategory.TYPE1.getValue();
    String DEFAULT_ADDRESS = RandomUtil.randomString(12);
    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);
    String UPDATED_ABBR = RandomUtil.randomString(8);
    String UPDATED_CATEGORY = TenantCategory.TYPE2.getValue();
    String UPDATED_AREA = RandomUtil.randomString(12);
    String UPDATED_ADDRESS = RandomUtil.randomString(12);
    String UPDATED_DESC = RandomUtil.randomString(12);

    static TenantBO createBO(JSONObject object) {
        var tenantBO = new TenantBO();
        tenantBO.setId(object.getLong("id"));
        tenantBO.setName(object.getStr("name", DEFAULT_NAME));
        tenantBO.setAbbr(object.getStr("abbr", DEFAULT_ABBR));
        tenantBO.setCategory(object.getStr("category", DEFAULT_CATEGORY));
        tenantBO.setArea(object.getStr("area", DEFAULT_AREA));
        tenantBO.setAddress(object.getStr("address", DEFAULT_ADDRESS));
        tenantBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return tenantBO;
    }

    static TenantBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("abbr", UPDATED_ABBR)
                .set("category", UPDATED_CATEGORY)
                .set("area", UPDATED_AREA)
                .set("address", UPDATED_ADDRESS)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static TenantBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static Tenant getEntity() {
        return Tenant.create()
                .orderBy(TENANT.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        return Opt.ofNullable(getEntity())
                .map(Tenant::getId)
                .get();
    }
}

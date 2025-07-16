package org.huangyalong.modules.example.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.example.domain.Example;

import java.io.Serializable;

import static org.huangyalong.modules.example.domain.table.ExampleTableDef.EXAMPLE;

public interface ExampleUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);

    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);

    String UPDATED_DESC = RandomUtil.randomString(12);

    static ExampleBO createBO(JSONObject object) {
        var exampleBO = new ExampleBO();
        exampleBO.setId(object.getLong("id"));
        exampleBO.setName(object.getStr("name", DEFAULT_NAME));
        exampleBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return exampleBO;
    }

    static ExampleBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static ExampleBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static Example getEntity() {
        return Example.create()
                .orderBy(EXAMPLE.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        return Opt.ofNullable(getEntity())
                .map(Example::getId)
                .get();
    }
}

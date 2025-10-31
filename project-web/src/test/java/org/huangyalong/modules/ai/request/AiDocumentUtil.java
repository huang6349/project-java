package org.huangyalong.modules.ai.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.huangyalong.modules.ai.domain.AiDocument;
import org.huangyalong.modules.file.request.FileUtil;

import java.io.Serializable;

import static org.huangyalong.modules.ai.domain.table.AiDocumentTableDef.AI_DOCUMENT;

public interface AiDocumentUtil {

    String DEFAULT_NAME = RandomUtil.randomString(12);
    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_NAME = RandomUtil.randomString(12);
    String UPDATED_DESC = RandomUtil.randomString(12);

    static AiDocumentBO createBO(JSONObject object) {
        var documentBO = new AiDocumentBO();
        documentBO.setId(object.getLong("id"));
        documentBO.setName(object.getStr("name", DEFAULT_NAME));
        documentBO.setFilename(FileUtil.getFilename());
        documentBO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return documentBO;
    }

    static AiDocumentBO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("name", UPDATED_NAME)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static AiDocumentBO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static AiDocument getEntity() {
        return AiDocument.create()
                .orderBy(AI_DOCUMENT.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(AiDocument::getId)
                .get();
    }
}

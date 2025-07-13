package org.huangyalong.modules.system.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@Accessors(chain = true)
public class PermAssocsData implements Serializable {

    private List<PermAssoc> assocs;

    private String assoc;

    private Long assocId;

    public PermAssocsData addPermIds(List<Long> permIds) {
        Opt.ofNullable(permIds)
                .get()
                .stream()
                .map(this::addPermId)
                .forEach(this::add);
        return this;
    }

    public PermAssocsData add(PermAssoc value) {
        if (CollUtil.isEmpty(assocs))
            assocs = CollUtil.newArrayList();
        CollUtil.addAll(assocs, value);
        return this;
    }

    PermAssoc addPermId(Long permId) {
        return PermAssoc.create()
                .setPermId(permId)
                .setAssoc(assoc)
                .setAssocId(assocId);
    }
}

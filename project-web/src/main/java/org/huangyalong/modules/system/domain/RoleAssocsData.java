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
public class RoleAssocsData implements Serializable {

    private List<RoleAssoc> assocs;

    private String assoc;

    private Long assocId;

    private Long tenantId;

    public RoleAssocsData addRoleIds(List<Long> roleIds) {
        Opt.ofNullable(roleIds)
                .orElse(CollUtil.newArrayList())
                .stream()
                .map(this::addRoleId)
                .forEach(this::add);
        return this;
    }

    public RoleAssocsData add(RoleAssoc value) {
        if (CollUtil.isEmpty(assocs))
            assocs = CollUtil.newArrayList();
        CollUtil.addAll(assocs, value);
        return this;
    }

    RoleAssoc addRoleId(Long roleId) {
        return RoleAssoc.create()
                .setRoleId(roleId)
                .setAssoc(assoc)
                .setAssocId(assocId)
                .setTenantId(tenantId);
    }
}

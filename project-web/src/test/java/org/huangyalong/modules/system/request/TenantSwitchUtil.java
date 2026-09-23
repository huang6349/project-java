package org.huangyalong.modules.system.request;

public interface TenantSwitchUtil {

    static TenantSwitchBO createBO() {
        var tenantId = TenantUtil.getId();
        var switchBO = new TenantSwitchBO();
        switchBO.setTenantId(tenantId);
        return switchBO;
    }
}

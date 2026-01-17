package org.huangyalong.core.constants;

public interface TenantConstants {

    /**
     * 无效租户编号（租户未获取时的占位值）
     */
    Long INVALID = 0L;


    /**
     * 忽略租户隔离（无租户身份）
     */
    Long NONE = -1L;
}

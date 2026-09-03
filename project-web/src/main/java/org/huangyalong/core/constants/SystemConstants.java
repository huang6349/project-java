package org.huangyalong.core.constants;

import org.huangyalong.modules.system.enums.ConfigRule;

import java.util.Map;

public interface SystemConstants {

    String CODE_TENANT = "tenant";

    String CODE_AI = "ai";

    String CODE_SYSTEM = "system";

    /**
     * code 验证规则表：RO 只读（yml 每次启动覆盖，接口禁止修改）、
     * RW 预置（yml 在数据库无该 code 记录时写入，之后接口允许修改）、
     * FR 自由（yml 不参与，接口允许修改）；
     * 未登记规则的 code 视为自由（yml 不参与，接口允许修改）
     */
    Map<String, ConfigRule> CODE_RULES = Map.of(
            CODE_TENANT, ConfigRule.RO,
            CODE_AI, ConfigRule.RO,
            CODE_SYSTEM, ConfigRule.FR
    );
}

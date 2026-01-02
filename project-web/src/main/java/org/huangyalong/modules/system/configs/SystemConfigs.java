package org.huangyalong.modules.system.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.myframework.base.domain.BaseConfigs;

import java.util.Map;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemConfigs extends BaseConfigs<SystemConfigs> {

    public static final String NAME_TENANT = "tenant";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public SystemConfigs addTenant(Map<String, Object> configs) {
        add(NAME_TENANT, configs);
        return self();
    }

    public SystemConfigs addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

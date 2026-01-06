package org.huangyalong.modules.system.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.myframework.base.domain.BaseConfigs;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TenantConfigs extends BaseConfigs<TenantConfigs> {

    public static final String NAME_ENABLED = "tenant.enabled";

    public static final String NAME_VERSION = "tenant.version";

    public static final String VERSION = "1.0.0";

    public TenantConfigs addEnabled(Boolean value) {
        add(NAME_ENABLED, value);
        return self();
    }

    public TenantConfigs addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

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
public class IotConfigs extends BaseConfigs<IotConfigs> {

    public static final String NAME_ENABLED = "enabled";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public IotConfigs addEnabled(Boolean value) {
        add(NAME_ENABLED, value);
        return self();
    }

    public IotConfigs addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

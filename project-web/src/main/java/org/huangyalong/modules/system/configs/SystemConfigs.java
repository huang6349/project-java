package org.huangyalong.modules.system.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.myframework.base.domain.BaseConfigs;

import static cn.hutool.core.lang.Opt.ofNullable;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemConfigs extends BaseConfigs<SystemConfigs> {

    public static final String NAME_TENANT = "tenant";

    public static final String NAME_AI = "ai";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public SystemConfigs addTenant(TenantConfigs configs) {
        add(NAME_TENANT, ofNullable(configs)
                .map(TenantConfigs::getConfigs)
                .get());
        return self();
    }

    public SystemConfigs addAi(AiConfigs configs) {
        add(NAME_AI, ofNullable(configs)
                .map(AiConfigs::getConfigs)
                .get());
        return self();
    }

    public SystemConfigs addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

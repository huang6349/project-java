package org.huangyalong.modules.system.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.myframework.base.domain.BaseExtras;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TenantExtras extends BaseExtras<TenantExtras> {

    public static final String NAME_ABBR = "abbr";

    public static final String NAME_AREA = "area";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public TenantExtras addAbbr(String value) {
        add(NAME_ABBR, value);
        return self();
    }

    public TenantExtras addArea(String value) {
        add(NAME_AREA, value);
        return self();
    }

    public TenantExtras addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

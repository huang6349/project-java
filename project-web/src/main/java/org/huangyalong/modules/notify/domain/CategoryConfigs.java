package org.huangyalong.modules.notify.domain;

import cn.hutool.core.lang.Opt;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huangyalong.modules.notify.enums.NotifyFreq;
import org.myframework.base.domain.BaseConfigs;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryConfigs extends BaseConfigs<CategoryConfigs> {

    public static final String NAME_FREQ = "freq";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public CategoryConfigs addFreq(Integer value) {
        add(NAME_FREQ, Opt.ofNullable(value)
                .map(NotifyFreq::fromValue)
                .map(NotifyFreq::getValue)
                .get());
        return self();
    }

    public CategoryConfigs addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

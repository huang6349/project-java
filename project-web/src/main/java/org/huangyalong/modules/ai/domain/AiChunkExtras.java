package org.huangyalong.modules.ai.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.myframework.base.domain.BaseExtras;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AiChunkExtras extends BaseExtras<AiChunkExtras> {

    public static final String NAME_ORIG_ID = "origId";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public AiChunkExtras addOrigId(String value) {
        add(NAME_ORIG_ID, value);
        return self();
    }

    public AiChunkExtras addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

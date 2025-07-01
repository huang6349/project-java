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
public class AiDocumentExtras extends BaseExtras<AiDocumentExtras> {

    public static final String NAME_FILENAME = "filename";

    public static final String NAME_EXT = "ext";

    public static final String NAME_CONTENT_TYPE = "contentType";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public AiDocumentExtras addFilename(String value) {
        add(NAME_FILENAME, value);
        return self();
    }

    public AiDocumentExtras addExt(String value) {
        add(NAME_EXT, value);
        return self();
    }

    public AiDocumentExtras addContentType(String value) {
        add(NAME_CONTENT_TYPE, value);
        return self();
    }

    public AiDocumentExtras addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

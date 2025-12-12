package org.huangyalong.modules.webhook.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huangyalong.modules.webhook.enums.WebhookTrigger;
import org.myframework.base.domain.BaseExtras;
import org.myframework.extra.webhook.WebhookFormat;

import static cn.hutool.core.lang.Opt.ofBlankAble;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WebhookExtras extends BaseExtras<WebhookExtras> {

    public static final String NAME_FORMAT = "format";

    public static final String NAME_SECRET = "secret";

    public static final String NAME_TRIGGER = "trigger";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public WebhookExtras addFormat(String value) {
        add(NAME_FORMAT, ofBlankAble(value)
                .map(WebhookFormat::fromValue)
                .map(WebhookFormat::getValue)
                .get());
        return self();
    }

    public WebhookExtras addSecret(String value) {
        add(NAME_SECRET, value);
        return self();
    }

    public WebhookExtras addTrigger(String value) {
        add(NAME_TRIGGER, ofBlankAble(value)
                .map(WebhookTrigger::fromValue)
                .map(WebhookTrigger::getValue)
                .get());
        return self();
    }

    public WebhookExtras addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

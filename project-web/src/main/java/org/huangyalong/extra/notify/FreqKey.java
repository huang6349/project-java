package org.huangyalong.extra.notify;

import cn.hutool.core.lang.Opt;
import lombok.Data;
import lombok.experimental.Accessors;
import org.myframework.extra.notify.NotifyPayload;

import static org.huangyalong.extra.notify.helper.NotifyHelper.DEFAULT_APP;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class FreqKey {

    private String tple;

    private String app = DEFAULT_APP;

    public static FreqKey of(NotifyPayload payload) {
        var tple = Opt.ofNullable(payload)
                .map(NotifyPayload::getTple)
                .get();
        var freqKey = FreqKey.of()
                .setTple(tple);
        Opt.ofNullable(payload)
                .map(NotifyPayload::getApp)
                .ifPresent(freqKey::setApp);
        return freqKey;
    }
}

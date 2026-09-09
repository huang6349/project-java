package org.huangyalong.extra.notify.processor;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.myframework.extra.notify.NotifyPayload;

import static java.lang.String.join;

/**
 * 控制台渠道负载(示例)
 * <p>
 * 载荷键约定:recipient/app/id 由发送门面注入,content=消息正文;
 * 模板固定 {@code Console}(记录回写归属该类别代码)
 */
@Data(staticConstructor = "create")
@Accessors(chain = true)
public class ConsolePayload implements NotifyPayload {

    private static final String SEP = ":";

    private String app;

    private String recipient;

    @Setter(AccessLevel.NONE)
    private String tple = "Console";

    private String id;

    private String content;

    @Override
    public String getChannel() {
        var tple = getTple();
        var recip = getRecipient();
        var id = getId();
        return join(SEP, tple, recip, id);
    }
}

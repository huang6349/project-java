package org.huangyalong.modules.notify.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import com.mybatisflex.core.row.DbChain;
import org.huangyalong.extra.notify.processor.ConsoleProcessor;
import org.myframework.extra.notify.NotifyData;
import org.myframework.extra.notify.NotifyMessage;
import org.myframework.extra.notify.NotifySendHelper;
import org.myframework.qdb.helper.QdbHelper;

import static org.huangyalong.core.constants.QdbConstants.NOTIFY_RECORD;

public interface RecordUtil {

    String DEFAULT_RECIPIENT = RandomUtil.randomNumbers(11);
    String DEFAULT_ID = RandomUtil.randomNumbers(16);
    String DEFAULT_CODE = "Console";
    String DEFAULT_CONTENT = RandomUtil.randomString(12);

    static void writeTestData() {
        var message = new NotifyMessage();
        message.addData(NotifyData.of("recipient", DEFAULT_RECIPIENT));
        message.addData(NotifyData.of("id", DEFAULT_ID));
        message.addData(NotifyData.of("content", DEFAULT_CONTENT));
        NotifySendHelper.send(ConsoleProcessor.class, message);
    }

    static String getId() {
        var query = DbChain.table(NOTIFY_RECORD);
        return Opt.ofNullable(QdbHelper.getOne(query))
                .map(it -> it.getString("id"))
                .get();
    }
}

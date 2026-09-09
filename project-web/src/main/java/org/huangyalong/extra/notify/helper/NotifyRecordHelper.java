package org.huangyalong.extra.notify.helper;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.huangyalong.modules.notify.domain.NotifyApp;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.myframework.extra.notify.NotifyPayload;
import org.myframework.qdb.domain.QdbEntity;
import org.myframework.qdb.helper.QdbHelper;

import static cn.hutool.core.text.CharSequenceUtil.maxLength;
import static org.huangyalong.core.constants.QdbConstants.NOTIFY_RECORD;
import static org.huangyalong.extra.notify.helper.NotifyHelper.DEFAULT_APP;
import static org.huangyalong.extra.notify.helper.NotifyHelper.DEFAULT_APP_NAME;
import static org.huangyalong.modules.notify.domain.table.NotifyAppTableDef.NOTIFY_APP;
import static org.huangyalong.modules.notify.domain.table.NotifyCategoryTableDef.NOTIFY_CATEGORY;
import static org.huangyalong.modules.notify.enums.NotifyStatus.TYPE1;
import static org.huangyalong.modules.notify.enums.NotifyStatus.TYPE2;

/**
 * 消息记录回写(静态,风格同 NotifyFreqHelper)
 * <p>
 * 发送结果经 ILP 写入 QuestDB {@code notify_record}：
 * 类别按负载 {@code tple}(类别代码)解析,来源应用名按 {@code app} 解析;
 * 类别缺失时静默跳过
 */
@UtilityClass
public class NotifyRecordHelper {

    public static void ok(NotifyPayload payload) {
        var category = Opt.ofNullable(payload)
                .map(NotifyRecordHelper::fetchCategory)
                .get();
        if (ObjectUtil.isNull(category)) return;
        var recipient = Opt.ofNullable(payload)
                .map(NotifyPayload::getRecipient)
                .get();
        var channel = Opt.ofNullable(payload)
                .map(NotifyPayload::getChannel)
                .get();
        var content = Opt.ofNullable(payload)
                .map(NotifyPayload::getContent)
                .get();
        var name = Opt.ofNullable(category)
                .map(NotifyCategory::getName)
                .get();
        var code = Opt.ofNullable(category)
                .map(NotifyCategory::getCode)
                .get();
        var appName = Opt.ofNullable(payload)
                .map(NotifyRecordHelper::fetchAppName)
                .orElse(DEFAULT_APP_NAME);
        var app = Opt.ofNullable(payload)
                .map(NotifyPayload::getApp)
                .orElse(DEFAULT_APP);
        QdbHelper.write(QdbEntity.of(NOTIFY_RECORD)
                .symbol("recipient", recipient)
                .symbol("channel", channel)
                .symbol("code", code)
                .symbol("app", app)
                .symbol("status", TYPE1)
                .set("name", name)
                .set("appName", appName)
                .set("content", content)
                .atNow());
    }

    public static void fail(NotifyPayload payload,
                            String desc) {
        var category = Opt.ofNullable(payload)
                .map(NotifyRecordHelper::fetchCategory)
                .get();
        if (ObjectUtil.isNull(category)) return;
        var recipient = Opt.ofNullable(payload)
                .map(NotifyPayload::getRecipient)
                .get();
        var channel = Opt.ofNullable(payload)
                .map(NotifyPayload::getChannel)
                .get();
        var content = Opt.ofNullable(payload)
                .map(NotifyPayload::getContent)
                .get();
        var name = Opt.ofNullable(category)
                .map(NotifyCategory::getName)
                .get();
        var code = Opt.ofNullable(category)
                .map(NotifyCategory::getCode)
                .get();
        var appName = Opt.ofNullable(payload)
                .map(NotifyRecordHelper::fetchAppName)
                .orElse(DEFAULT_APP_NAME);
        var app = Opt.ofNullable(payload)
                .map(NotifyPayload::getApp)
                .orElse(DEFAULT_APP);
        QdbHelper.write(QdbEntity.of(NOTIFY_RECORD)
                .symbol("recipient", recipient)
                .symbol("channel", channel)
                .symbol("code", code)
                .symbol("app", app)
                .symbol("status", TYPE2)
                .set("name", name)
                .set("appName", appName)
                .set("content", content)
                .set("desc", maxLength(desc, 512))
                .atNow());
    }

    private static NotifyCategory fetchCategory(NotifyPayload payload) {
        var tple = Opt.ofNullable(payload)
                .map(NotifyPayload::getTple)
                .get();
        return NotifyCategory.create()
                .where(NOTIFY_CATEGORY.CODE.eq(tple))
                .one();
    }

    private static String fetchAppName(NotifyPayload payload) {
        var app = Opt.ofNullable(payload)
                .map(NotifyPayload::getApp)
                .get();
        if (StrUtil.isNotBlank(app)) {
            return NotifyApp.create()
                    .select(NOTIFY_APP.NAME)
                    .where(NOTIFY_APP.CODE.eq(app))
                    .oneAs(String.class);
        } else return null;
    }
}

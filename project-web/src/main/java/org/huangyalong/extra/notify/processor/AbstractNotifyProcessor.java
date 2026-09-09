package org.huangyalong.extra.notify.processor;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import org.huangyalong.extra.notify.FreqKey;
import org.huangyalong.extra.notify.helper.NotifyHelper;
import org.myframework.extra.notify.NotifyPayload;
import org.myframework.extra.notify.NotifyProcessor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.huangyalong.extra.notify.helper.NotifyRecordHelper.fail;
import static org.huangyalong.extra.notify.helper.NotifyRecordHelper.ok;
import static org.myframework.core.redis.RedisHelper.getExpire;
import static org.myframework.core.redis.RedisHelper.setEx;

/**
 * 消息处理器骨架(纯 SPI,无 Spring 注入)
 * <p>
 * 发送编排：解析负载 -> 限频检查 -> 渠道发送 -> 记录频次 -> 回写成功记录;
 * 任一异常回写失败记录(类别/应用解析失败时记录静默跳过)
 */
public abstract class AbstractNotifyProcessor<T extends NotifyPayload> implements NotifyProcessor<T> {

    @Override
    public void recordFreq(T message) {
        var channel = Opt.ofNullable(message)
                .map(NotifyPayload::getChannel)
                .get();
        var tple = Opt.ofNullable(message)
                .map(NotifyPayload::getTple)
                .get();
        var freq = Opt.ofNullable(message)
                .map(FreqKey::of)
                .map(NotifyHelper::getFreq)
                .get();
        if (ObjectUtil.isNull(freq) || freq <= 0) return;
        setEx(channel, tple, freq, TimeUnit.MINUTES);
    }

    @Override
    public boolean checkFreq(T message) {
        try {
            var channel = Opt.ofNullable(message)
                    .map(NotifyPayload::getChannel)
                    .get();
            var expire = getExpire(channel, TimeUnit.SECONDS);
            return ObjectUtil.isNull(expire) || expire <= 0;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public void send(Map<String, Object> message) {
        try {
            var payload = getPayload(message);
            var valid = checkFreq(payload);
            if (BooleanUtil.isFalse(valid)) return;
            send(payload);
            recordFreq(payload);
            ok(payload);
        } catch (Exception e) {
            var payload = getPayload(message);
            var desc = e.getMessage();
            fail(payload, desc);
        }
    }
}

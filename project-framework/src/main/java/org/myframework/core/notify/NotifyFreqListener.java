package org.myframework.core.notify;

import cn.hutool.core.util.ObjectUtil;
import com.github.likavn.eventbus.core.annotation.EventbusListener;
import com.github.likavn.eventbus.core.api.MsgListener;
import com.github.likavn.eventbus.core.metadata.data.Message;
import org.myframework.core.redis.RedisHelper;
import org.springframework.stereotype.Service;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.myframework.core.constants.Subscribe.NOTIFY_FREQ_LISTENER;

@Service
@EventbusListener(codes = NOTIFY_FREQ_LISTENER)
public class NotifyFreqListener implements MsgListener<String> {

    @Override
    public void onMessage(Message<String> message) {
        var messageBody = message.getBody();
        if (ObjectUtil.isNotEmpty(messageBody)) {
            var keys = RedisHelper.keys(format("notify:{}:*", messageBody));
            RedisHelper.delete(keys);
        }
    }
}

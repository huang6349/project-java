package org.huangyalong.core.satoken.listener;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import static cn.hutool.core.convert.Convert.toStr;
import static org.huangyalong.core.satoken.helper.TenantHelper.load;

@Component
public class TenantListener implements MessageListener {

    @Override
    public void onMessage(@NotNull Message message,
                          byte[] pattern) {
        load(toStr(message.getBody()));
    }
}

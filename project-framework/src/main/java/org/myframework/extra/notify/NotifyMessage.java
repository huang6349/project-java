package org.myframework.extra.notify;

import cn.hutool.core.map.MapUtil;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * 消息载体(拼装 data 后经 SPI 处理器分发)
 */
@Getter
public class NotifyMessage implements Serializable {

    private Map<String, Object> data;

    public void addData(NotifyData datum) {
        if (MapUtil.isEmpty(data))
            data = MapUtil.newHashMap();
        var name = datum.getName();
        var o = datum.getO();
        data.put(name, o);
    }
}

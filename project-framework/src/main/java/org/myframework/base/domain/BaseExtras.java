package org.myframework.base.domain;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class BaseExtras<T extends BaseExtras<T>> implements Serializable {

    private Map<String, Object> extras;

    private JSONObject object;

    public Map<String, Object> getExtras() {
        if (MapUtil.isEmpty(object))
            object = JSONUtil.parseObj(extras);
        var raw = object.getRaw();
        return MapUtil.removeNullValue(raw);
    }

    public T setExtras(Map<String, Object> extras) {
        this.extras = extras;
        return self();
    }

    public T add(String expression, Object value) {
        if (MapUtil.isEmpty(object))
            object = JSONUtil.parseObj(extras);
        object.putByPath(expression, value);
        return self();
    }

    public T self() {
        var $lombok$self = (T) this;
        return $lombok$self;
    }
}

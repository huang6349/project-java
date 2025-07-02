package org.myframework.base.domain;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings({"UnnecessaryLocalVariable", "unchecked", "unused"})
public abstract class BaseConfigs<T extends BaseConfigs<T>> implements Serializable {

    private Map<String, Object> configs;

    private JSONObject object;

    public Map<String, Object> getConfigs() {
        if (MapUtil.isEmpty(object))
            object = JSONUtil.parseObj(configs);
        var raw = object.getRaw();
        return MapUtil.removeNullValue(raw);
    }

    public T setConfigs(Map<String, Object> configs) {
        this.configs = configs;
        return self();
    }

    public T add(String expression, Object value) {
        if (MapUtil.isEmpty(object))
            object = JSONUtil.parseObj(configs);
        object.putByPath(expression, value);
        return self();
    }

    public T self() {
        var $lombok$self = (T) this;
        return $lombok$self;
    }
}

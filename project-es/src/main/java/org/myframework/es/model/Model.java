package org.myframework.es.model;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class Model<T extends Model<T>> implements Serializable {

    public T self() {
        var $lombok$self = (T) this;
        return $lombok$self;
    }
}

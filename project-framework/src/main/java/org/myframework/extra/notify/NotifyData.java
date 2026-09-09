package org.myframework.extra.notify;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息数据项
 */
@Data(staticConstructor = "of")
public class NotifyData implements Serializable {

    private final String name;

    private final Object o;
}

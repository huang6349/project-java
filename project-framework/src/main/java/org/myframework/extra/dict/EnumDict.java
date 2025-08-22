package org.myframework.extra.dict;

import static org.myframework.extra.dict.IsDefault.YES;

public interface EnumDict<T> {

    String getLabel();

    T getValue();

    Integer getSort();

    Integer getIsDefault();

    Integer getStyle();

    static <Enum extends EnumDict<T>, T> Enum fromValue(T value, Class<Enum> enumClass) {
        for (var enumInstance : enumClass.getEnumConstants())
            if (enumInstance.getValue().equals(value))
                return enumInstance;
        for (var enumInstance : enumClass.getEnumConstants())
            if (enumInstance.getIsDefault().equals(YES.getValue()))
                return enumInstance;
        return null;
    }
}

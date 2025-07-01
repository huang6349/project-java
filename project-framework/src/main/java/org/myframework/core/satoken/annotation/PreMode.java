package org.myframework.core.satoken.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PreMode {
    AND("满足全部"),
    OR("部分满足");

    private final String label;
}

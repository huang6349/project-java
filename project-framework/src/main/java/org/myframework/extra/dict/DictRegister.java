package org.myframework.extra.dict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DictRegister implements ImportBeanDefinitionRegistrar {

    private static final Set<String> SCANNED_PACKAGES = new HashSet<>();
    private static final String DEFAULT_PACKAGE = "org.myframework";

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata metadata,
                                        @NotNull BeanDefinitionRegistry registry) {
        // 始终添加默认包路径
        SCANNED_PACKAGES.add(DEFAULT_PACKAGE);
        // 获取 @DictScan 注解的属性
        var annotationName = DictScan.class.getName();
        var attributes = metadata.getAnnotationAttributes(annotationName);
        if (attributes != null) {
            var packages = (String[]) attributes.get("value");
            if (ArrayUtil.isNotEmpty(packages)) {
                CollUtil.addAll(SCANNED_PACKAGES, packages);
            }
        }
    }

    public static Set<String> getScannedPackages() {
        // 确保始终包含默认包路径
        SCANNED_PACKAGES.add(DEFAULT_PACKAGE);
        return Collections.unmodifiableSet(SCANNED_PACKAGES);
    }
}

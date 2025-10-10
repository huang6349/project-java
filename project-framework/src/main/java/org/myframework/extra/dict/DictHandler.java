package org.myframework.extra.dict;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cn.hutool.core.util.ClassUtil.scanPackageByAnnotation;

@Component
public class DictHandler implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // 获取所有需要扫描的包路径
        Set<String> packages = DictRegister.getScannedPackages();
        // 扫描每个包路径下的带有 @Dict 注解的类
        packages.forEach(packagePath -> scanPackageByAnnotation(packagePath, Dict.class)
                .stream()
                .filter(clazz -> clazz.isEnum() && EnumDict.class.isAssignableFrom(clazz))
                .map(DictUtil::parse)
                .forEach(DictCache::add));
    }
}

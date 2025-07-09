package org.myframework.base.web;

import cn.hutool.core.util.TypeUtil;
import com.mybatis.flex.reactor.core.ReactorService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "LombokGetterMayBeUsed", "unchecked"})
public abstract class SuperSimpleController<S extends ReactorService<Entity>, Entity> implements BaseController<Entity> {

    @Autowired
    @Getter
    protected S baseService;

    @Override
    public Class<Entity> getEntityClass() {
        var superType = getClass().getGenericSuperclass();
        var type = TypeUtil.getTypeArgument(superType, 1);
        return (Class<Entity>) TypeUtil.getClass(type);
    }
}

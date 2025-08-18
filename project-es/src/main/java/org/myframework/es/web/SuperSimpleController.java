package org.myframework.es.web;

import cn.hutool.core.util.TypeUtil;
import lombok.Getter;
import org.myframework.es.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class SuperSimpleController<S extends EsService<Entity>, Entity> implements BaseController<Entity> {

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

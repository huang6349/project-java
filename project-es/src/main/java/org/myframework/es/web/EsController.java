package org.myframework.es.web;

import cn.hutool.core.util.TypeUtil;
import org.myframework.es.service.EsService;
import org.myframework.es.web.curd.QueryController;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public abstract class EsController<
        S extends EsService<Entity>,
        Id extends Serializable,
        Entity,
        Queries>
        extends SuperSimpleController<S, Entity>
        implements QueryController<Entity, Id, Queries> {

    @Override
    public Class<Entity> getEntityClass() {
        var superType = getClass().getGenericSuperclass();
        var type = TypeUtil.getTypeArgument(superType, 2);
        return (Class<Entity>) TypeUtil.getClass(type);
    }
}

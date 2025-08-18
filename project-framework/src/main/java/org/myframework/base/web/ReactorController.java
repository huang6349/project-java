package org.myframework.base.web;

import cn.hutool.core.util.TypeUtil;
import com.mybatis.flex.reactor.core.ReactorService;
import org.myframework.base.web.curd.*;

import java.io.Serializable;

public abstract class ReactorController<
        S extends ReactorService<Entity>,
        Id extends Serializable,
        Entity,
        Queries,
        SaveBO,
        UpdateBO>
        extends SuperSimpleController<S, Entity>
        implements QueryController<Entity, Id, Queries>,
        SaveController<Entity, SaveBO>,
        UpdateController<Entity, UpdateBO>,
        DeleteController<Entity, Id> {

    @Override
    public Class<Entity> getEntityClass() {
        var superType = getClass().getGenericSuperclass();
        var type = TypeUtil.getTypeArgument(superType, 2);
        return (Class<Entity>) TypeUtil.getClass(type);
    }
}

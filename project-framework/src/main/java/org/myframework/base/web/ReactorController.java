package org.myframework.base.web;

import cn.hutool.core.util.TypeUtil;
import com.mybatis.flex.reactor.core.ReactorService;
import org.myframework.base.web.reactive.*;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public abstract class ReactorController<
        S extends ReactorService<Entity>,
        Id extends Serializable,
        Entity,
        Queries,
        SaveBO,
        UpdateBO>
        extends ReactiveController<S, Entity>
        implements ReactiveQueryController<Entity, Id, Queries>,
        ReactiveSaveController<Entity, SaveBO>,
        ReactiveUpdateController<Entity, UpdateBO>,
        ReactiveDeleteController<Entity, Id> {

    @Override
    public Class<Entity> getEntityClass() {
        var superType = getClass().getGenericSuperclass();
        var type = TypeUtil.getTypeArgument(superType, 2);
        return (Class<Entity>) TypeUtil.getClass(type);
    }
}

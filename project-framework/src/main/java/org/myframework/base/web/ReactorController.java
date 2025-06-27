package org.myframework.base.web;

import cn.hutool.core.util.TypeUtil;
import org.myframework.base.service.ReactorService;
import org.myframework.base.web.reactive.ReactiveController;
import org.myframework.base.web.reactive.ReactiveDeleteController;
import org.myframework.base.web.reactive.ReactiveSaveController;
import org.myframework.base.web.reactive.ReactiveUpdateController;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public abstract class ReactorController<S extends ReactorService<Entity>, Id extends Serializable, Entity, SaveBO, UpdateBO>
        extends ReactiveController<S, Entity>
        implements ReactiveSaveController<Entity, SaveBO>,
        ReactiveUpdateController<Entity, UpdateBO>,
        ReactiveDeleteController<Entity, Id> {

    @Override
    public Class<Entity> getEntityClass() {
        var superType = getClass().getGenericSuperclass();
        var type = TypeUtil.getTypeArgument(superType, 2);
        return (Class<Entity>) TypeUtil.getClass(type);
    }
}

package org.myframework.base.web.reactive;

import cn.hutool.core.util.TypeUtil;
import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.service.IService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "LombokGetterMayBeUsed", "unchecked"})
public abstract class ReactiveController<S extends ReactorService<Entity>, Entity> implements ReactiveBaseController<Entity> {

    @Autowired
    @Getter
    protected S reactorService;

    @Override
    public IService<Entity> getBaseService() {
        return getReactorService()
                .getBlockService();
    }

    @Override
    public Class<Entity> getEntityClass() {
        var superType = getClass().getGenericSuperclass();
        var type = TypeUtil.getTypeArgument(superType, 1);
        return (Class<Entity>) TypeUtil.getClass(type);
    }
}

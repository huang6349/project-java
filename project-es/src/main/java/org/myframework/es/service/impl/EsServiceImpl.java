package org.myframework.es.service.impl;

import lombok.Getter;
import org.dromara.easyes.core.kernel.BaseEsMapper;
import org.myframework.es.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "LombokGetterMayBeUsed"})
public abstract class EsServiceImpl<Mapper extends BaseEsMapper<Entity>, Entity>
        implements EsService<Entity> {

    @Autowired
    @Getter
    protected Mapper mapper;
}

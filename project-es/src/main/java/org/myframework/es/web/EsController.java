package org.myframework.es.web;

import lombok.Getter;
import org.myframework.es.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "LombokGetterMayBeUsed"})
public abstract class EsController<S extends EsService<Entity>, Entity> {

    @Autowired
    @Getter
    protected S service;
}

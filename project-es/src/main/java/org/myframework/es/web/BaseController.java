package org.myframework.es.web;

import org.myframework.es.service.EsService;

@SuppressWarnings("unused")
public interface BaseController<Entity> {

    EsService<Entity> getBaseService();

    Class<Entity> getEntityClass();
}

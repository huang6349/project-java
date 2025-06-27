package org.myframework.base.web;

import com.mybatisflex.core.service.IService;

public interface BaseController<Entity> {

    IService<Entity> getBaseService();

    Class<Entity> getEntityClass();
}

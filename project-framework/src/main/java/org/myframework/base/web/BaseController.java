package org.myframework.base.web;

import com.mybatis.flex.reactor.core.ReactorService;

public interface BaseController<Entity> {

    ReactorService<Entity> getBaseService();

    Class<Entity> getEntityClass();
}

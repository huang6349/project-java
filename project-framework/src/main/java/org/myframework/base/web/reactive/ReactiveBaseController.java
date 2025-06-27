package org.myframework.base.web.reactive;

import org.myframework.base.service.ReactorService;
import org.myframework.base.web.BaseController;

public interface ReactiveBaseController<Entity> extends BaseController<Entity> {

    ReactorService<Entity> getReactorService();
}

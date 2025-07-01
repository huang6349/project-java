package org.myframework.base.web.reactive;

import com.mybatis.flex.reactor.core.ReactorService;
import org.myframework.base.web.BaseController;

public interface ReactiveBaseController<Entity> extends BaseController<Entity> {

    ReactorService<Entity> getReactorService();
}

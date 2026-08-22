package org.myframework.qdb.web;

import org.myframework.qdb.service.QdbService;
import org.myframework.qdb.web.curd.QueryController;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class QdbController<
        S extends QdbService,
        Id extends Serializable,
        Queries>
        extends SuperSimpleController<S>
        implements QueryController<
        Id,
        Queries> {
}

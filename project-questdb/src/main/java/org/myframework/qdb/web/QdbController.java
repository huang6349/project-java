package org.myframework.qdb.web;

import org.myframework.qdb.web.curd.QueryController;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class QdbController<
        Id extends Serializable,
        Queries>
        extends SuperSimpleController
        implements QueryController<
        Id,
        Queries> {
}

package org.myframework.qdb.web;

import org.myframework.qdb.service.QdbService;

@SuppressWarnings("unused")
public interface BaseController {

    QdbService getBaseService();

    String getTableName();
}

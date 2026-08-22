package org.myframework.qdb.web;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.myframework.qdb.annotation.QdbTable;
import org.myframework.qdb.service.QdbService;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.hutool.core.annotation.AnnotationUtil.getAnnotation;
import static cn.hutool.core.text.CharSequenceUtil.removeSuffix;
import static cn.hutool.core.text.CharSequenceUtil.toUnderlineCase;
import static cn.hutool.core.util.ClassUtil.getClassName;
import static java.lang.Boolean.TRUE;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class SuperSimpleController<S extends QdbService> implements BaseController {

    @Autowired
    @Getter
    protected S baseService;

    @Override
    public String getTableName() {
        var annotation = getAnnotation(getClass(), QdbTable.class);
        return Opt.ofNullable(annotation)
                .map(QdbTable::value)
                .filter(StrUtil::isNotBlank)
                .orElseGet(this::inferTableName);
    }

    /**
     * 无 @QdbTable 注解时按约定推断表名：从控制器类名去 "Controller" 后缀转下划线
     */
    String inferTableName() {
        var simpleName = getClassName(getClass(), TRUE);
        var className = removeSuffix(simpleName, "Controller");
        return toUnderlineCase(className);
    }
}

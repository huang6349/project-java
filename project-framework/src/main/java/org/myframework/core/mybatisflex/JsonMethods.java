package org.myframework.core.mybatisflex;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.core.query.FunctionQueryColumn;
import com.mybatisflex.core.query.QueryColumn;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatisflex.core.FlexGlobalConfig.getDefaultConfig;
import static com.mybatisflex.core.query.QueryMethods.string;

public interface JsonMethods {

    static QueryColumn extract(QueryColumn columnS,
                               String path) {
        var fnName = "JSON_EXTRACT";
        var s = format("$.{}", path);
        return new FunctionQueryColumn(fnName,
                columnS,
                string(s)
        );
    }

    static QueryColumn unquote(QueryColumn columnS) {
        var fnName = "JSON_UNQUOTE";
        return new FunctionQueryColumn(fnName,
                columnS
        );
    }

    static QueryColumn value(QueryColumn columnS,
                             String path) {
        var fnName = "JSON_VALUE";
        var s = format("$.{}", path);
        return new FunctionQueryColumn(fnName,
                columnS,
                string(s)
        );
    }

    static QueryColumn ue(QueryColumn columnS,
                          String path) {
        var dbType = DialectFactory.getHintDbType();
        if (ObjectUtil.isNull(dbType))
            dbType = getDefaultConfig().getDbType();
        if (dbType == DbType.MYSQL)
            return unquote(extract(columnS, path));
        return value(columnS, path);
    }
}

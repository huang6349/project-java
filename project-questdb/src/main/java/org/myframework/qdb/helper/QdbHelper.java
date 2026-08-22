package org.myframework.qdb.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.datasource.DataSourceKey;
import io.questdb.client.Sender;
import org.myframework.core.exception.BusinessException;
import org.myframework.qdb.domain.QdbEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static cn.hutool.core.convert.Convert.*;

/**
 * QuestDB 写入助手
 * <p>
 * 提供 {@link #write} 与 {@link #runInQdb} 两个静态方法
 */
public class QdbHelper extends AbstractQdbHelper {

    /**
     * 在 QuestDB 数据源上下文中执行 supplier，自动管理 DataSourceKey 切换
     */
    public static <T> T runInQdb(Supplier<T> supplier) {
        try {
            DataSourceKey.use("questdb");
            return supplier.get();
        } finally {
            DataSourceKey.clear();
        }
    }

    /**
     * 写入一个 {@link QdbEntity}，自动管理 Sender 生命周期（borrow + flush + close）
     * <p>
     * id 缺省时补充雪花字符串，timestamp 缺省时补充当前时间，
     * 列值为 {@code null} 的项写入前自动剔除；
     * 表名为空或未设置任何列时直接抛出业务异常
     */
    public static void write(QdbEntity entity) {
        StaticLog.trace("写入时序数据");
        validateWrite(entity);
        try (var sender = getQuestDB().borrowSender()) {
            var timestamp = Opt.ofNullable(entity)
                    .map(QdbEntity::getTimestamp)
                    .orElseGet(Instant::now);
            var id = Opt.ofNullable(entity)
                    .map(QdbEntity::getId)
                    .orElseGet(IdUtil::getSnowflakeNextIdStr);
            sender.table(entity.getTable());
            sender.stringColumn("id", id);
            setSymbols(sender, entity);
            setColumns(sender, entity);
            sender.at(timestamp);
            sender.flush();
        }
    }

    /**
     * 校验写入实体：表名非空且至少设置一列数据
     */
    private static void validateWrite(QdbEntity entity) {
        validateTable(entity);
        validateColumns(entity);
    }

    /**
     * 校验表名非空
     */
    private static void validateTable(QdbEntity entity) {
        var table = Opt.ofNullable(entity)
                .map(QdbEntity::getTable)
                .get();
        if (StrUtil.isNotBlank(table)) return;
        throw new BusinessException("表名不能为空");
    }

    /**
     * 校验至少设置一列数据（tag 列或普通列）
     */
    private static void validateColumns(QdbEntity entity) {
        var symbols = Opt.ofNullable(entity)
                .map(QdbEntity::getSymbols)
                .get();
        var columns = Opt.ofNullable(entity)
                .map(QdbEntity::getColumns)
                .get();
        if (CollUtil.isNotEmpty(symbols)) return;
        if (CollUtil.isNotEmpty(columns)) return;
        throw new BusinessException("至少设置一列数据");
    }

    /**
     * 写入实体的全部 tag 列（symbol），自动剔除 null 值项
     */
    private static void setSymbols(Sender sender,
                                   QdbEntity entity) {
        var entrySet = Opt.ofNullable(entity)
                .map(QdbEntity::getSymbols)
                .map(MapUtil::removeNullValue)
                .map(Map::entrySet)
                .orElseGet(Collections::emptySet);
        for (var entry : entrySet) {
            var name = entry.getKey();
            var value = entry.getValue();
            sender.symbol(name, value);
        }
    }

    /**
     * 写入实体的全部普通列，自动剔除 null 值项
     */
    private static void setColumns(Sender sender,
                                   QdbEntity entity) {
        var entrySet = Opt.ofNullable(entity)
                .map(QdbEntity::getColumns)
                .map(MapUtil::removeNullValue)
                .map(Map::entrySet)
                .orElseGet(Collections::emptySet);
        for (var entry : entrySet) {
            var name = entry.getKey();
            var value = entry.getValue();
            setColumn(sender, name, value);
        }
    }

    /**
     * 按列值运行时类型分发到对应的 Sender column 方法，
     * 整数家族统一写 long 列（ILP 线上等价）、浮点家族统一写 double 列，
     * 其余未明确支持的类型经 hutool Convert 垫底转为字符串
     */
    private static void setColumn(Sender sender,
                                  String name,
                                  Object value) {
        if (value instanceof CharSequence v) {
            sender.stringColumn(name, v);
        } else if (value instanceof Character v) {
            sender.charColumn(name, v);
        } else if (value instanceof Boolean v) {
            sender.boolColumn(name, v);
        } else if (value instanceof Instant v) {
            sender.timestampColumn(name, v);
        } else if (value instanceof UUID v) {
            var lo = v.getLeastSignificantBits();
            var hi = v.getMostSignificantBits();
            sender.uuidColumn(name, lo, hi);
        } else if (value instanceof byte[] v) {
            sender.binaryColumn(name, v);
        } else if (value instanceof BigDecimal v) {
            sender.decimalColumn(name, v.toPlainString());
        } else if (value instanceof Float || value instanceof Double) {
            sender.doubleColumn(name, toDouble(value));
        } else if (value instanceof Number v) {
            sender.longColumn(name, toLong(v));
        } else sender.stringColumn(name, toStr(value));
    }
}

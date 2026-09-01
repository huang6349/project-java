package org.myframework.qdb.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.row.DbChain;
import com.mybatisflex.core.row.Row;
import io.questdb.client.Sender;
import org.myframework.core.exception.BusinessException;
import org.myframework.qdb.domain.QdbEntity;
import org.myframework.qdb.response.QdbPageVO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cn.hutool.core.convert.Convert.*;
import static java.lang.Boolean.FALSE;

/**
 * QuestDB 助手
 * <p>
 * 提供 {@link #write} 写入与 {@link #getOne}/{@link #list}/{@link #listAfter} 查询等静态方法
 */
public class QdbHelper extends AbstractQdbHelper {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String ID_KEY = "id";

    public static final String TIMESTAMP_KEY = "timestamp";

    private static final QueryColumn ID_COLUMN = new QueryColumn(ID_KEY);

    private static final QueryColumn TIMESTAMP_COLUMN = new QueryColumn(TIMESTAMP_KEY);

    // ===== 查询（查）操作 =====

    /**
     * 根据查询条件查询最新一条数据（{@code ORDER BY timestamp DESC LIMIT 1}）
     * <p>
     * 与 {@link #listAfter(Integer, String, DbChain)} 区别：该方法取精确 1 条，后者取最新一页
     */
    public static Row getOne(DbChain query) {
        StaticLog.trace("根据查询条件查询一条数据");
        var wrapper = requireQuery(query);
        wrapper.orderBy(TIMESTAMP_COLUMN, FALSE);
        wrapper.limit(1);
        return runInQdb(wrapper::one);
    }

    /**
     * 根据查询条件查询数据集合
     */
    public static List<Row> list(DbChain query) {
        StaticLog.trace("根据查询条件查询数据集合");
        var wrapper = requireQuery(query);
        return runInQdb(wrapper::list);
    }

    /**
     * 键集分页：按 id 游标取前 size 条（大数据量时序浏览推荐）
     * cursor 为 null 返回最新 size 条；响应中 cursor 为下一页游标（最近一条的 id），null 表示无更多数据
     * <p>
     * id 为雪花字符串（等长十进制，字典序即数值序），保证单调递增且唯一，
     * 游标边界精确无丢行；排序仍按 timestamp（人类可读时间序）。
     * 注意：手动指定 id 时需保证其单调性，否则边界与时间序可能错位
     */
    public static QdbPageVO<Row> listAfter(Integer pageSize,
                                           String cursor,
                                           DbChain query) {
        StaticLog.trace("键集分页查询数据: {}", query);
        var wrapper = requireQuery(query);
        var limit = Opt.ofNullable(pageSize)
                .orElse(DEFAULT_PAGE_SIZE);
        if (StrUtil.isNotBlank(cursor))
            wrapper.where(ID_COLUMN.lt(cursor));
        wrapper.orderBy(TIMESTAMP_COLUMN, FALSE);
        wrapper.limit(limit);
        var list = runInQdb(wrapper::list);
        var nextCursor = cursorOf(list, limit);
        return new QdbPageVO<Row>()
                .setList(list)
                .setCursor(nextCursor);
    }

    /**
     * 校验查询条件非空
     */
    private static DbChain requireQuery(DbChain query) {
        if (ObjectUtil.isNull(query)) {
            throw new BusinessException("查询条件不能为空");
        } else return query;
    }

    /**
     * 提取下一页游标：列表为空或结果数未满 limit（无更多数据）时返回 null
     */
    private static String cursorOf(List<Row> list,
                                   int limit) {
        if (CollUtil.isNotEmpty(list) && list.size() >= limit) {
            return CollUtil.getLast(list)
                    .getString(ID_KEY);
        } else return null;
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
            sender.stringColumn(ID_KEY, id);
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

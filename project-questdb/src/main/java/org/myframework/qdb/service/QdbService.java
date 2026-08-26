package org.myframework.qdb.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.row.DbChain;
import com.mybatisflex.core.row.Row;
import org.myframework.core.exception.BusinessException;
import org.myframework.qdb.response.QdbPageVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static cn.hutool.core.text.CharSequenceUtil.removeSuffix;
import static cn.hutool.core.text.CharSequenceUtil.toUnderlineCase;
import static cn.hutool.core.util.ClassUtil.getClassName;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.myframework.qdb.helper.QdbHelper.runInQdb;

public interface QdbService {

    int DEFAULT_PAGE_SIZE = 10;

    String ID_KEY = "id";

    String TIMESTAMP_KEY = "timestamp";

    QueryColumn ID_COLUMN = new QueryColumn(ID_KEY);

    QueryColumn TIMESTAMP_COLUMN = new QueryColumn(TIMESTAMP_KEY);

    /**
     * 按实现类名推断表名并返回查询 Chain：
     * 去掉 {@code Service}/{@code Impl} 后缀，转下划线
     */
    default DbChain getQueryChain() {
        var simpleName = getClassName(this.getClass(), TRUE);
        var serviceName = removeSuffix(simpleName, "Impl");
        var className = removeSuffix(serviceName, "Service");
        return DbChain.table(toUnderlineCase(className));
    }

    // ===== 查询（查）操作 =====

    /**
     * 根据查询条件查询最新一条数据（{@code ORDER BY timestamp DESC LIMIT 1}）
     * <p>
     * 与 {@link #listAfter(Integer, String, DbChain)} 区别：该方法取精确 1 条，后者取最新一页
     */
    default Mono<Row> getOne(DbChain query) {
        StaticLog.trace("根据查询条件查询一条数据");
        return Mono.fromCallable(() -> {
            var wrapper = requireQuery(query);
            wrapper.orderBy(TIMESTAMP_COLUMN, FALSE);
            wrapper.limit(1);
            return runInQdb(wrapper::one);
        });
    }

    default Flux<Row> list(DbChain query) {
        StaticLog.trace("根据查询条件查询数据集合");
        return Mono.fromCallable(() -> {
            var wrapper = requireQuery(query);
            return runInQdb(wrapper::list);
        }).flatMapMany(Flux::fromIterable);
    }

    /**
     * 键集分页：按 id 游标取前 size 条（大数据量时序浏览推荐）
     * cursor 为 null 返回最新 size 条；响应中 cursor 为下一页游标（最近一条的 id），null 表示无更多数据
     * <p>
     * id 为雪花字符串（等长十进制，字典序即数值序），保证单调递增且唯一，
     * 游标边界精确无丢行；排序仍按 timestamp（人类可读时间序）。
     * 注意：手动指定 id 时需保证其单调性，否则边界与时间序可能错位
     */
    default Mono<QdbPageVO<Row>> listAfter(Integer pageSize,
                                           String cursor,
                                           DbChain query) {
        StaticLog.trace("键集分页查询数据: {}", query);
        return Mono.fromCallable(() -> {
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
        });
    }

    /**
     * 校验查询条件非空
     */
    private DbChain requireQuery(DbChain query) {
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
}

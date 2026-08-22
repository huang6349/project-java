package org.myframework.qdb.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * QuestDB 写入实体（Map 风格轻量容器）
 * <p>
 * 参考 hutool-db Entity 的链式风格统一 ILP 写入参数：
 * <pre>{@code
 * QdbHelper.write(QdbEntity.of("metrics")
 *         .symbol("city", "BJ")
 *         .set("temp", 25.5)
 *         .at(Instant.now()));
 * }</pre>
 * {@code id} 缺省时写入前自动补充雪花字符串，{@code timestamp} 缺省时自动补当前时间；
 * 列值为 {@code null} 时写入前自动剔除（QuestDB 语义：缺省即 NULL）
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QdbEntity implements Serializable {

    /**
     * 表名
     */
    private String table;

    /**
     * 主键，缺省自动补充雪花字符串
     */
    private String id;

    /**
     * designated timestamp，缺省自动补充当前时间
     */
    @Setter(AccessLevel.NONE)
    private Instant timestamp;

    /**
     * tag 列（symbol），仅支持 {@link #symbol(String, String)} 追加
     */
    @Setter(AccessLevel.NONE)
    private Map<String, String> symbols = new LinkedHashMap<>();

    /**
     * 普通列，仅支持 {@link #set(String, Object)} 追加
     */
    @Setter(AccessLevel.NONE)
    private Map<String, Object> columns = new LinkedHashMap<>();

    /**
     * 构建指定表名的写入实体
     */
    public static QdbEntity of(String table) {
        return new QdbEntity()
                .setTable(table);
    }

    /**
     * 追加 tag 列（symbol）
     */
    public QdbEntity symbol(String name,
                            String value) {
        symbols.put(name, value);
        return this;
    }

    /**
     * 追加普通列
     * <p>
     * 支持 CharSequence / Character / Boolean / Instant / UUID /
     * BigDecimal / byte[] 及各类 Number（整数统一写 long 列、浮点统一写 double 列），
     * 其余类型经 hutool Convert 垫底转为字符串
     */
    public QdbEntity set(String name,
                         Object value) {
        columns.put(name, value);
        return this;
    }

    /**
     * 指定 designated timestamp，缺省时写入前自动补充当前时间
     */
    public QdbEntity at(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 使用当前时间作为 designated timestamp
     */
    public QdbEntity atNow() {
        var timestamp = Instant.now();
        return at(timestamp);
    }
}

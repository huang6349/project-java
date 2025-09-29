package org.myframework.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.autotable.annotation.AutoColumn;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class SuperEntity<E extends SuperEntity<E, T>, T> extends Model<E> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @AutoColumn(comment = "数据主键", sort = 1)
    @Schema(description = "数据主键")
    protected T id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @AutoColumn(comment = "创建时间", notNull = true)
    @Column(onInsertValue = "now()")
    @Schema(description = "创建时间")
    protected Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @AutoColumn(comment = "更新时间", notNull = true)
    @Column(onUpdateValue = "now()", onInsertValue = "now()")
    @Schema(description = "更新时间")
    protected Date updateTime;

    @JsonIgnore
    @AutoColumn(comment = "更新版本", defaultValue = "0")
    @Schema(description = "更新版本")
    protected Long version;
}

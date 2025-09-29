package org.myframework.base.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.autotable.annotation.AutoColumn;
import org.myframework.core.enums.IsDeleted;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Entity<E extends Entity<E, T>, T> extends SuperEntity<E, T> {

    @JsonIgnore
    @AutoColumn(comment = "是否删除", sort = -1, type = TINYINT, defaultValue = "0")
    @Schema(description = "是否删除")
    protected IsDeleted isDeleted;
}

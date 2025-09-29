package org.huangyalong.modules.notify.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;
import org.huangyalong.modules.notify.enums.CategoryStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "消息类别")
@Table(value = "tb_notify_category")
@Schema(name = "消息类别")
public class NotifyCategory extends Entity<NotifyCategory, Long> {

    @AutoColumn(comment = "类别名称", notNull = true)
    @Schema(description = "类别名称")
    private String name;

    @AutoColumn(comment = "类别代码", notNull = true)
    @Schema(description = "类别代码")
    private String code;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "类别模型", type = TEXT)
    @Schema(description = "类别模型")
    private Map<String, Object> metadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "配置信息", type = TEXT)
    @Schema(description = "配置信息")
    private Map<String, Object> configs;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "额外信息", type = TEXT)
    @Schema(description = "额外信息")
    private Map<String, Object> extras;

    @AutoColumn(comment = "备注", length = 512)
    @Schema(description = "备注")
    private String desc;

    @JKDictFormat
    @AutoColumn(comment = "类别状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "类别状态")
    private CategoryStatus status;
}

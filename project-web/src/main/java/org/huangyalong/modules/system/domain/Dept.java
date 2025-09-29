package org.huangyalong.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.huangyalong.modules.system.enums.DeptStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "部门信息")
@Table(value = "tb_dept")
@Schema(name = "部门信息")
public class Dept extends Entity<Dept, Long> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @AutoColumn(comment = "父级节点(父级主键)", defaultValue = "0")
    @Schema(description = "父级主键")
    private Long parentId;

    @AutoColumn(comment = "节点路径", notNull = true, length = 512)
    @Schema(description = "节点路径")
    private String path;

    @AutoColumn(comment = "节点顺序", defaultValue = "0")
    @Schema(description = "节点顺序")
    private Integer sort;

    @AutoColumn(comment = "部门名称", notNull = true)
    @Schema(description = "部门名称")
    private String name;

    @AutoColumn(comment = "部门代码", notNull = true)
    @Schema(description = "部门代码")
    private String code;

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
    @AutoColumn(comment = "部门状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "部门状态")
    private DeptStatus status;

    @Column(tenantId = true)
    @JsonIgnore
    @AutoColumn(comment = "租户主键(所属租户)", notNull = true)
    @Schema(description = "租户主键")
    private Long tenantId;
}

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
import org.myframework.base.domain.SuperEntity;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "租户记录")
@Table(value = "tb_tenant_record")
@Schema(name = "租户记录")
public class TenantRecord extends SuperEntity<TenantRecord, Long> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @AutoColumn(comment = "租户主键(所属租户)", notNull = true)
    @Schema(description = "租户主键")
    private Long tenantId;

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
}

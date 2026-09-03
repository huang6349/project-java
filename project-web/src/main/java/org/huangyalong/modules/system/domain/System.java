package org.huangyalong.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;
import org.huangyalong.modules.system.request.SystemBO;
import org.myframework.base.domain.SuperEntity;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "系统配置")
@Table(value = "tb_system")
@Schema(name = "系统配置")
public class System extends SuperEntity<System, Long> {

    @AutoColumn(comment = "配置代码", notNull = true)
    @Schema(description = "配置代码")
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

    /****************** with ******************/

    public System with(SystemBO systemBO) {
        Opt.ofNullable(systemBO)
                .map(SystemBO::getCode)
                .ifPresent(this::setCode);
        Opt.ofNullable(systemBO)
                .map(SystemBO::getConfigs)
                .ifPresent(this::setConfigs);
        return this;
    }
}

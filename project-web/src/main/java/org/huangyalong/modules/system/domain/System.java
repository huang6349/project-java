package org.huangyalong.modules.system.domain;

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
import org.huangyalong.modules.system.configs.AiConfigs;
import org.huangyalong.modules.system.configs.SystemConfigs;
import org.huangyalong.modules.system.configs.TenantConfigs;
import org.myframework.base.domain.SuperEntity;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.huangyalong.core.constants.SystemConstants.CONFIG_ID;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "系统信息")
@Table(value = "tb_system")
@Schema(name = "系统信息")
public class System extends SuperEntity<System, Long> {

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

    public System with(TenantConfigs configs) {
        setConfigs(SystemConfigs.create()
                .setConfigs(getConfigs())
                .addTenant(configs)
                .getConfigs());
        setId(CONFIG_ID);
        return this;
    }

    public System with(AiConfigs configs) {
        setConfigs(SystemConfigs.create()
                .setConfigs(getConfigs())
                .addAi(configs)
                .getConfigs());
        setId(CONFIG_ID);
        return this;
    }
}

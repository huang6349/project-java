package org.huangyalong.modules.system.domain;

import cn.hutool.core.lang.Opt;
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
import org.huangyalong.modules.system.enums.RoleStatus;
import org.huangyalong.modules.system.request.RoleBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "角色信息")
@Table(value = "tb_role")
@Schema(name = "角色信息")
public class Role extends Entity<Role, Long> {

    @AutoColumn(comment = "角色名称", notNull = true)
    @Schema(description = "角色名称")
    private String name;

    @AutoColumn(comment = "角色代码", notNull = true)
    @Schema(description = "角色代码")
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
    @AutoColumn(comment = "角色状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "角色状态")
    private RoleStatus status;

    /****************** with ******************/

    public Role with(RoleBO roleBO) {
        Opt.ofNullable(roleBO)
                .map(RoleBO::getName)
                .ifPresent(this::setName);
        Opt.ofNullable(roleBO)
                .map(RoleBO::getCode)
                .ifPresent(this::setCode);
        Opt.ofNullable(roleBO)
                .map(RoleBO::getDesc)
                .ifPresent(this::setDesc);
        return this;
    }
}

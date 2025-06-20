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
import org.huangyalong.modules.system.enums.RoleStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_role")
@Schema(name = "角色信息")
public class Role extends Entity<Role, Long> {

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色代码")
    private String code;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "配置信息")
    private Map<String, Object> configs;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "额外信息")
    private Map<String, Object> extras;

    @Schema(description = "备注")
    private String desc;

    @JKDictFormat
    @Schema(description = "角色状态")
    private RoleStatus status;
}

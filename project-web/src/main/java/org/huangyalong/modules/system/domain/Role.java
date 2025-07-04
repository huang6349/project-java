package org.huangyalong.modules.system.domain;

import cn.hutool.core.lang.Dict;
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
import org.huangyalong.modules.system.enums.RoleStatus;
import org.huangyalong.modules.system.request.RoleBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

@Data(staticConstructor = "create")
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
    private Dict configs;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "额外信息")
    private Dict extras;

    @Schema(description = "备注")
    private String desc;

    @JKDictFormat
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

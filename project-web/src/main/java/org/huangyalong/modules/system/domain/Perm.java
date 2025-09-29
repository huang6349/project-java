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
import org.huangyalong.modules.system.enums.PermStatus;
import org.huangyalong.modules.system.request.PermBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "权限信息")
@Table(value = "tb_perm")
@Schema(name = "权限信息")
public class Perm extends Entity<Perm, Long> {

    @AutoColumn(comment = "权限名称", notNull = true)
    @Schema(description = "权限名称")
    private String name;

    @AutoColumn(comment = "权限代码", notNull = true)
    @Schema(description = "权限代码")
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
    @AutoColumn(comment = "权限状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "权限状态")
    private PermStatus status;

    /****************** with ******************/

    public Perm with(PermBO permBO) {
        Opt.ofNullable(permBO)
                .map(PermBO::getName)
                .ifPresent(this::setName);
        Opt.ofNullable(permBO)
                .map(PermBO::getCode)
                .ifPresent(this::setCode);
        Opt.ofNullable(permBO)
                .map(PermBO::getDesc)
                .ifPresent(this::setDesc);
        return this;
    }
}

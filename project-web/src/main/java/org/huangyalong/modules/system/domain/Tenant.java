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
import org.huangyalong.modules.system.enums.TenantCategory;
import org.huangyalong.modules.system.enums.TenantStatus;
import org.huangyalong.modules.system.request.TenantBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "租户信息")
@Table(value = "tb_tenant")
@Schema(name = "租户信息")
public class Tenant extends Entity<Tenant, Long> {

    @AutoColumn(comment = "租户名称", notNull = true)
    @Schema(description = "租户名称")
    private String name;

    @AutoColumn(comment = "租户代码", notNull = true)
    @Schema(description = "租户代码")
    private String code;

    @JKDictFormat
    @AutoColumn(comment = "租户类别", type = TINYINT, defaultValue = "0")
    @Schema(description = "租户类别")
    private TenantCategory category;

    @AutoColumn(comment = "租户地址")
    @Schema(description = "租户地址")
    private String address;

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
    @AutoColumn(comment = "租户状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "租户状态")
    private TenantStatus status;

    /****************** view ******************/

    @Column(ignore = true)
    @Schema(description = "租户简称")
    private String abbr;

    @Column(ignore = true)
    @Schema(description = "租户地区")
    private String area;

    /****************** with ******************/

    public Tenant with(TenantBO tenantBO) {
        var abbr = Opt.ofNullable(tenantBO)
                .map(TenantBO::getAbbr)
                .get();
        var area = Opt.ofNullable(tenantBO)
                .map(TenantBO::getArea)
                .get();
        Opt.ofNullable(tenantBO)
                .map(TenantBO::getName)
                .ifPresent(this::setName);
        Opt.ofNullable(tenantBO)
                .map(TenantBO::getCategory)
                .map(TenantCategory::fromValue)
                .ifPresent(this::setCategory);
        Opt.ofNullable(tenantBO)
                .map(TenantBO::getAddress)
                .ifPresent(this::setAddress);
        Opt.ofNullable(tenantBO)
                .map(TenantBO::getDesc)
                .ifPresent(this::setDesc);
        setExtras(TenantExtras.create()
                .setExtras(getExtras())
                .addAbbr(abbr)
                .addArea(area)
                .addVersion()
                .getExtras());
        return this;
    }
}

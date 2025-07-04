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
import org.huangyalong.modules.system.enums.TenantCategory;
import org.huangyalong.modules.system.enums.TenantStatus;
import org.huangyalong.modules.system.request.TenantBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.myframework.extra.dict.EnumDict.fromValue;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_tenant")
@Schema(name = "租户信息")
public class Tenant extends Entity<Tenant, Long> {

    @Schema(description = "租户名称")
    private String name;

    @Schema(description = "租户代码")
    private String code;

    @JKDictFormat
    @Schema(description = "租户类别")
    private TenantCategory category;

    @Schema(description = "租户地址")
    private String address;

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

    @SuppressWarnings("DuplicatedCode")
    public Tenant with(TenantBO tenantBO) {
        var category = Opt.ofNullable(tenantBO)
                .map(TenantBO::getCategory)
                .get();
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
                .map(TenantBO::getAddress)
                .ifPresent(this::setAddress);
        Opt.ofNullable(tenantBO)
                .map(TenantBO::getDesc)
                .ifPresent(this::setDesc);
        setCategory(fromValue(category, TenantCategory.class));
        setExtras(TenantExtras.create()
                .setExtras(getExtras())
                .addAbbr(abbr)
                .addArea(area)
                .addVersion()
                .getExtras());
        return this;
    }
}

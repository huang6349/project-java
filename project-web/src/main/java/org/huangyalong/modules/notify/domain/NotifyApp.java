package org.huangyalong.modules.notify.domain;

import cn.hutool.core.lang.Opt;
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
import org.huangyalong.modules.notify.enums.CategoryStatus;
import org.huangyalong.modules.notify.enums.NotifyFreq;
import org.huangyalong.modules.notify.request.AppBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "消息应用")
@Table(value = "tb_notify_app")
@Schema(name = "消息应用")
public class NotifyApp extends Entity<NotifyApp, Long> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @AutoColumn(comment = "类别主键(所属类别)", notNull = true)
    @Schema(description = "类别主键")
    private Long categoryId;

    @AutoColumn(comment = "应用名称", notNull = true)
    @Schema(description = "应用名称")
    private String name;

    @AutoColumn(comment = "应用代码", notNull = true)
    @Schema(description = "应用代码")
    private String code;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "应用模型", type = TEXT)
    @Schema(description = "应用模型")
    private Map<String, Object> metadata;

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
    @AutoColumn(comment = "应用状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "应用状态")
    private CategoryStatus status;

    /****************** view ******************/

    @Column(ignore = true)
    @JKDictFormat
    @Schema(description = "消息频率")
    private NotifyFreq freq;

    /****************** with ******************/

    public NotifyApp with(AppBO appBO) {
        Opt.ofNullable(appBO)
                .map(AppBO::getCategoryId)
                .ifPresent(this::setCategoryId);
        Opt.ofNullable(appBO)
                .map(AppBO::getName)
                .ifPresent(this::setName);
        Opt.ofNullable(appBO)
                .map(AppBO::getDesc)
                .ifPresent(this::setDesc);
        setConfigs(CategoryConfigs.create()
                .setConfigs(getConfigs())
                .addFreq(appBO.getFreq())
                .addVersion()
                .getConfigs());
        return this;
    }
}

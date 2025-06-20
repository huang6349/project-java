package org.huangyalong.modules.notify.domain;

import cn.hutool.core.lang.Dict;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huangyalong.modules.notify.enums.CategoryStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_notify_category")
@Schema(name = "消息类别")
public class NotifyCategory extends Entity<NotifyCategory, Long> {

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "类别代码")
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
    @Schema(description = "类别状态")
    private CategoryStatus status;
}

package org.huangyalong.modules.example.domain;

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
import org.huangyalong.modules.example.enums.ExampleStatus;
import org.huangyalong.modules.example.request.ExampleBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "示例信息")
@Table(value = "tb_example")
@Schema(name = "示例信息")
public class Example extends Entity<Example, Long> {

    @AutoColumn(comment = "示例名称", notNull = true)
    @Schema(description = "示例名称")
    private String name;

    @AutoColumn(comment = "示例代码", notNull = true)
    @Schema(description = "示例代码")
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
    @AutoColumn(comment = "示例状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "示例状态")
    private ExampleStatus status;

    @Column(tenantId = true)
    @JsonIgnore
    @AutoColumn(comment = "租户主键(所属租户)", notNull = true)
    @Schema(description = "租户主键")
    private Long tenantId;

    /****************** with ******************/

    public Example with(ExampleBO exampleBO) {
        Opt.ofNullable(exampleBO)
                .map(ExampleBO::getName)
                .ifPresent(this::setName);
        Opt.ofNullable(exampleBO)
                .map(ExampleBO::getDesc)
                .ifPresent(this::setDesc);
        return this;
    }
}

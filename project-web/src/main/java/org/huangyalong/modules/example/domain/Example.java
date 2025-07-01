package org.huangyalong.modules.example.domain;

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
import org.huangyalong.modules.example.enums.ExampleStatus;
import org.huangyalong.modules.example.request.ExampleBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_example")
@Schema(name = "示例信息")
public class Example extends Entity<Example, Long> {

    @Schema(description = "示例名称")
    private String name;

    @Schema(description = "示例代码")
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
    @Schema(description = "示例状态")
    private ExampleStatus status;

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

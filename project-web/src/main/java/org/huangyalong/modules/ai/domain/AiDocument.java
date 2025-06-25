package org.huangyalong.modules.ai.domain;

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
import org.huangyalong.modules.ai.enums.AiDocumentStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_ai_document")
@Schema(name = "文档信息")
public class AiDocument extends Entity<AiDocument, Long> {

    @Schema(description = "文档名称")
    private String name;

    @Schema(description = "文档代码")
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
    @Schema(description = "文档状态")
    private AiDocumentStatus status;

    /****************** view ******************/

    @Column(ignore = true)
    @Schema(description = "文件名称")
    private String filename;

    @Column(ignore = true)
    @Schema(description = "文件扩展名")
    private String ext;

    @Column(ignore = true)
    @Schema(description = "MIME类型")
    private String contentType;
}

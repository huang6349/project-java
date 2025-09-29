package org.huangyalong.modules.ai.domain;

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
import org.myframework.base.domain.SuperEntity;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "分片信息")
@Table(value = "tb_ai_chunk")
@Schema(name = "分片信息")
public class AiChunk extends SuperEntity<AiChunk, Long> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @AutoColumn(comment = "文档主键(所属文档)", notNull = true)
    @Schema(description = "文档主键")
    private Long documentId;

    @AutoColumn(comment = "分片内容", type = TEXT, notNull = true)
    @Schema(description = "分片内容")
    private String content;

    @AutoColumn(comment = "分片顺序", defaultValue = "0")
    @Schema(description = "分片顺序")
    private Integer sort;

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
}

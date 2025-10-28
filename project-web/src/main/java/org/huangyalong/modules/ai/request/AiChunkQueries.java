package org.huangyalong.modules.ai.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分片信息-Queries")
public class AiChunkQueries extends BaseQueries {

    @Schema(description = "文档主键")
    private Long documentId;
}

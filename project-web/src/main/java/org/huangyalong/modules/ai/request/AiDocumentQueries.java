package org.huangyalong.modules.ai.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文档信息-Queries")
public class AiDocumentQueries extends BaseQueries {

    @Schema(description = "文档名称")
    private String name;

    @Schema(description = "文档代码")
    private String code;
}

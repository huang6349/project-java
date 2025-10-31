package org.huangyalong.modules.file.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文件信息-Queries")
public class FileQueries extends BaseQueries {

    @Schema(description = "原始文件名")
    private String origFilename;

    @Schema(description = "文件扩展名")
    private String ext;
}

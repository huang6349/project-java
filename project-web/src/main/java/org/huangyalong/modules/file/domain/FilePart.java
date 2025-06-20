package org.huangyalong.modules.file.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.x.file.storage.core.hash.HashInfo;
import org.myframework.base.domain.Entity;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_file_part")
@Schema(name = "文件分片")
public class FilePart extends Entity<FilePart, Long> {

    @Schema(description = "存储平台")
    private String platform;

    @Schema(description = "分片ETag")
    private String eTag;

    @Schema(description = "分片号。每一个上传的分片都有一个分片号，一般情况下取值范围是1~10000")
    private Integer partNumber;

    @Schema(description = "文件大小，单位字节")
    private Long partSize;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "哈希信息")
    private HashInfo hashInfo;

    @Schema(description = "上传ID，仅在手动分片上传时使用")
    private String uploadId;
}

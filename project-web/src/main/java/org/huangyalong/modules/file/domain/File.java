package org.huangyalong.modules.file.domain;

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
import org.dromara.x.file.storage.core.hash.HashInfo;
import org.huangyalong.modules.file.enums.FileStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_file")
@Schema(name = "文件信息")
public class File extends Entity<File, Long> {

    @Schema(description = "文件访问地址")
    private String url;

    @Schema(description = "文件大小，单位字节")
    private Long size;

    @Schema(description = "文件名称")
    private String filename;

    @Schema(description = "原始文件名")
    private String origFilename;

    @JsonIgnore
    @Schema(description = "基础存储路径")
    private String basePath;

    @JsonIgnore
    @Schema(description = "存储路径")
    private String path;

    @Schema(description = "文件扩展名")
    private String ext;

    @Schema(description = "MIME类型")
    private String contentType;

    @JsonIgnore
    @Schema(description = "存储平台")
    private String platform;

    @JsonIgnore
    @Schema(description = "缩略图访问路径")
    private String thUrl;

    @JsonIgnore
    @Schema(description = "缩略图名称")
    private String thFilename;

    @JsonIgnore
    @Schema(description = "缩略图大小，单位字节")
    private Long thSize;

    @JsonIgnore
    @Schema(description = "缩略图MIME类型")
    private String thContentType;

    @JsonIgnore
    @Schema(description = "文件所属对象ID")
    private String objectId;

    @JsonIgnore
    @Schema(description = "文件所属对象类型，例如用户头像，评价图片")
    private String objectType;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "文件元数据")
    private Map<String, String> metadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "文件用户元数据")
    private Map<String, String> userMetadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "缩略图元数据")
    private Map<String, String> thMetadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "缩略图用户元数据")
    private Map<String, String> thUserMetadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "附加属性")
    private Dict attr;

    @JsonIgnore
    @Schema(description = "文件ACL")
    private Object fileAcl;

    @JsonIgnore
    @Schema(description = "缩略图文件ACL")
    private Object thFileAcl;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "哈希信息")
    private HashInfo hashInfo;

    @JsonIgnore
    @Schema(description = "上传ID，仅在手动分片上传时使用")
    private String uploadId;

    @JsonIgnore
    @Schema(description = "上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成")
    private Integer uploadStatus;

    @JKDictFormat
    @Schema(description = "文件状态")
    private FileStatus status;
}

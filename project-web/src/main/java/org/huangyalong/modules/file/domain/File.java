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
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;
import org.dromara.x.file.storage.core.hash.HashInfo;
import org.huangyalong.modules.file.enums.FileStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.*;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "文件信息")
@Table(value = "tb_file")
@Schema(name = "文件信息")
public class File extends Entity<File, Long> {

    @AutoColumn(comment = "文件访问地址", notNull = true, length = 512)
    @Schema(description = "文件访问地址")
    private String url;

    @AutoColumn(comment = "文件大小，单位字节")
    @Schema(description = "文件大小，单位字节")
    private Long size;

    @AutoColumn(comment = "文件名称")
    @Schema(description = "文件名称")
    private String filename;

    @AutoColumn(comment = "原始文件名")
    @Schema(description = "原始文件名")
    private String origFilename;

    @JsonIgnore
    @AutoColumn(comment = "基础存储路径")
    @Schema(description = "基础存储路径")
    private String basePath;

    @JsonIgnore
    @AutoColumn(comment = "存储路径")
    @Schema(description = "存储路径")
    private String path;

    @AutoColumn(comment = "文件扩展名", length = 32)
    @Schema(description = "文件扩展名")
    private String ext;

    @AutoColumn(comment = "MIME类型", length = 128)
    @Schema(description = "MIME类型")
    private String contentType;

    @JsonIgnore
    @AutoColumn(comment = "存储平台", length = 32)
    @Schema(description = "存储平台")
    private String platform;

    @JsonIgnore
    @AutoColumn(comment = "缩略图访问路径", length = 512)
    @Schema(description = "缩略图访问路径")
    private String thUrl;

    @JsonIgnore
    @AutoColumn(comment = "缩略图名称")
    @Schema(description = "缩略图名称")
    private String thFilename;

    @JsonIgnore
    @AutoColumn(comment = "缩略图大小，单位字节")
    @Schema(description = "缩略图大小，单位字节")
    private Long thSize;

    @JsonIgnore
    @AutoColumn(comment = "缩略图MIME类型", length = 128)
    @Schema(description = "缩略图MIME类型")
    private String thContentType;

    @JsonIgnore
    @AutoColumn(comment = "文件所属对象ID", length = 32)
    @Schema(description = "文件所属对象ID")
    private String objectId;

    @JsonIgnore
    @AutoColumn(comment = "文件所属对象类型，例如用户头像，评价图片", length = 32)
    @Schema(description = "文件所属对象类型，例如用户头像，评价图片")
    private String objectType;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "文件元数据", type = TEXT)
    @Schema(description = "文件元数据")
    private Map<String, String> metadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "文件用户元数据", type = TEXT)
    @Schema(description = "文件用户元数据")
    private Map<String, String> userMetadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "缩略图元数据", type = TEXT)
    @Schema(description = "缩略图元数据")
    private Map<String, String> thMetadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "缩略图用户元数据", type = TEXT)
    @Schema(description = "缩略图用户元数据")
    private Map<String, String> thUserMetadata;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "附加属性", type = TEXT)
    @Schema(description = "附加属性")
    private Dict attr;

    @JsonIgnore
    @AutoColumn(comment = "文件ACL", type = VARCHAR, length = 32)
    @Schema(description = "文件ACL")
    private Object fileAcl;

    @JsonIgnore
    @AutoColumn(comment = "缩略图文件ACL", type = VARCHAR, length = 32)
    @Schema(description = "缩略图文件ACL")
    private Object thFileAcl;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @AutoColumn(comment = "哈希信息", type = TEXT)
    @Schema(description = "哈希信息")
    private HashInfo hashInfo;

    @JsonIgnore
    @AutoColumn(comment = "上传ID，仅在手动分片上传时使用", length = 128)
    @Schema(description = "上传ID，仅在手动分片上传时使用")
    private String uploadId;

    @JsonIgnore
    @AutoColumn(comment = "上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成")
    @Schema(description = "上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成")
    private Integer uploadStatus;

    @JKDictFormat
    @AutoColumn(comment = "文件状态", type = TINYINT, defaultValue = "0")
    @Schema(description = "文件状态")
    private FileStatus status;
}

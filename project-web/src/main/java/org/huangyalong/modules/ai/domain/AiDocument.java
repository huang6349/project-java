package org.huangyalong.modules.ai.domain;

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
import org.huangyalong.modules.ai.enums.AiDocumentStatus;
import org.huangyalong.modules.ai.request.AiDocumentBO;
import org.huangyalong.modules.file.domain.File;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Map;

import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TEXT;
import static org.dromara.autotable.annotation.mysql.MysqlTypeConstant.TINYINT;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "文档信息")
@Table(value = "tb_ai_document")
@Schema(name = "文档信息")
public class AiDocument extends Entity<AiDocument, Long> {

    @AutoColumn(comment = "文档名称", notNull = true)
    @Schema(description = "文档名称")
    private String name;

    @AutoColumn(comment = "文档代码", notNull = true)
    @Schema(description = "文档代码")
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
    @AutoColumn(comment = "文档状态", type = TINYINT, defaultValue = "0")
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

    /****************** with ******************/

    public AiDocument with(AiDocumentBO documentBO) {
        Opt.ofNullable(documentBO)
                .map(AiDocumentBO::getName)
                .ifPresent(this::setName);
        Opt.ofNullable(documentBO)
                .map(AiDocumentBO::getDesc)
                .ifPresent(this::setDesc);
        return this;
    }

    public AiDocument with(File file) {
        var filename = Opt.ofNullable(file)
                .map(File::getFilename)
                .get();
        var ext = Opt.ofNullable(file)
                .map(File::getExt)
                .get();
        var contentType = Opt.ofNullable(file)
                .map(File::getContentType)
                .get();
        setExtras(AiDocumentExtras.create()
                .setExtras(getExtras())
                .addFilename(filename)
                .addExt(ext)
                .addContentType(contentType)
                .addVersion()
                .getExtras());
        return this;
    }
}

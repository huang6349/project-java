package org.huangyalong.modules.notify.domain;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.FieldType;
import org.huangyalong.modules.notify.enums.NotifyStatus;
import org.myframework.es.model.Model;
import org.myframework.extra.jackson.JKDictFormat;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@IndexName("es_notify_record")
@Schema(name = "消息记录")
public class NotifyRecord extends Model<NotifyRecord> {

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "类别代码")
    private String code;

    @Schema(description = "消息地址")
    private String recipient;

    @Schema(description = "消息渠道")
    private String channel;

    @Schema(description = "消息内容")
    private String content;

    @JKDictFormat
    @IndexField(fieldType = FieldType.KEYWORD)
    @Schema(description = "消息状态")
    private NotifyStatus status;

    @Schema(description = "备注")
    private String desc;

    @IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @Schema(description = "推送时间")
    private String createTime = DateUtil.now();
}

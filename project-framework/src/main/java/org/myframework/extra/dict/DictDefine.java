package org.myframework.extra.dict;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@Schema(name = "字典信息(类别)")
public class DictDefine implements Serializable {

    @Schema(description = "字典名称")
    private String name;

    @JsonIgnore
    @Schema(description = "字典类别")
    private String category;

    @JsonIgnore
    @Schema(name = "字典信息")
    private List<ItemDefine> items;
}

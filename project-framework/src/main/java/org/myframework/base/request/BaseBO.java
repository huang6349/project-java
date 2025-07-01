package org.myframework.base.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.myframework.base.validation.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class BaseBO<T> implements Serializable {

    @NotNull(message = "主键不能为空", groups = Update.class)
    @Schema(description = "数据主键")
    protected T id;
}

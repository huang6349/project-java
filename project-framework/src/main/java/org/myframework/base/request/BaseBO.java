package org.myframework.base.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class BaseBO<T> implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "数据主键")
    protected T id;
}

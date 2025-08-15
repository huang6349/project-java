package org.huangyalong.modules.system.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.huangyalong.modules.system.domain.Perm;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Schema(name = "角色权限-VO")
public class RolePermVO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "数据主键")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "权限主键")
    private List<Long> permIds;

    /****************** with ******************/

    public RolePermVO with(List<Perm> perms) {
        setPermIds(Stream.ofNullable(perms)
                .flatMap(Collection::stream)
                .map(Perm::getId)
                .toList());
        return this;
    }
}

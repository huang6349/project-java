package org.huangyalong.modules.system.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.huangyalong.modules.system.domain.Role;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Schema(name = "用户角色-VO")
public class UserRoleVO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "数据主键")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "租户主键")
    private Long tenantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "角色主键")
    private List<Long> roleIds;

    /****************** with ******************/

    public UserRoleVO with(List<Role> roles) {
        setRoleIds(Stream.ofNullable(roles)
                .flatMap(Collection::stream)
                .map(Role::getId)
                .toList());
        return this;
    }
}

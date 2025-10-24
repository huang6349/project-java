package org.huangyalong.modules.system.response;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.huangyalong.modules.system.domain.User;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Schema(name = "授权信息")
public class Authentication implements Serializable {

    @Schema(description = "用户信息")
    private User user;

    @Schema(description = "权限列表")
    private List<String> perms;

    @Schema(description = "角色列表")
    private List<String> roles;

    public static Authentication create(User user) {
        var authentication = new Authentication();
        authentication.setUser(user);
        var perms = StpUtil.getPermissionList();
        authentication.setPerms(perms);
        var roles = StpUtil.getRoleList();
        authentication.setRoles(roles);
        return authentication;
    }
}

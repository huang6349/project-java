package org.huangyalong.modules.system.response;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.huangyalong.core.satoken.helper.UserHelper;

import java.io.Serializable;

@AllArgsConstructor
@Data
@ToString
@Schema(name = "令牌信息")
public class JWTToken implements Serializable {

    @JsonProperty("id_token")
    @Schema(description = "授权令牌")
    private String idToken;

    public static JWTToken create(Long userId) {
        StpUtil.login(userId);
        var idToken = UserHelper.getToken();
        return new JWTToken(idToken);
    }
}

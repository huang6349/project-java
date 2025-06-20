package org.huangyalong.modules.system.domain;

import cn.hutool.core.lang.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huangyalong.modules.system.enums.UserStatus;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Date;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "tb_user")
@Schema(name = "用户信息")
public class User extends Entity<User, Long> {

    @Schema(description = "用户帐号")
    private String username;

    @JsonIgnore
    @Schema(description = "用户密码")
    private String password;

    @JsonIgnore
    @Schema(description = "盐")
    private String salt;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "用户邮箱")
    private String email;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "配置信息")
    private Dict configs;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "额外信息")
    private Dict extras;

    @Schema(description = "备注")
    private String desc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "登录时间")
    private Date loginTime;

    @JKDictFormat
    @Schema(description = "用户状态")
    private UserStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "默认租户")
    private Long tenantId;
}

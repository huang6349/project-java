package org.huangyalong.modules.system.domain;

import cn.hutool.core.lang.Opt;
import cn.hutool.crypto.digest.BCrypt;
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
import org.huangyalong.modules.system.enums.UserGender;
import org.huangyalong.modules.system.enums.UserStatus;
import org.huangyalong.modules.system.request.UserBO;
import org.myframework.base.domain.Entity;
import org.myframework.extra.jackson.JKDictFormat;

import java.util.Date;
import java.util.Map;

@Data(staticConstructor = "create")
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
    private Map<String, Object> configs;

    @Column(typeHandler = JacksonTypeHandler.class)
    @JsonIgnore
    @Schema(description = "额外信息")
    private Map<String, Object> extras;

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

    /****************** view ******************/

    @Column(ignore = true)
    @Schema(description = "用户昵称")
    private String nickname;

    @Column(ignore = true)
    @Schema(description = "用户头像")
    private String avatar;

    @Column(ignore = true)
    @JKDictFormat
    @Schema(description = "用户性别")
    private UserGender gender;

    @Column(ignore = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "用户生日")
    private Date birthday;

    @Column(ignore = true)
    @Schema(description = "用户地址")
    private String address;

    /****************** with ******************/

    public User with(UserBO userBO) {
        Opt.ofNullable(userBO)
                .map(UserBO::getUsername)
                .ifPresent(this::setUsername);
        setSalt(Opt.ofNullable(getSalt())
                .orElse(BCrypt.gensalt()));
        Opt.ofNullable(userBO)
                .map(UserBO::getPassword1)
                .map(password -> BCrypt.hashpw(password, getSalt()))
                .ifPresent(this::setPassword);
        Opt.ofNullable(userBO)
                .map(UserBO::getMobile)
                .ifPresent(this::setMobile);
        Opt.ofNullable(userBO)
                .map(UserBO::getEmail)
                .ifPresent(this::setEmail);
        setExtras(UserExtras.create()
                .setExtras(getExtras())
                .addNickname(userBO.getNickname())
                .addAvatar(userBO.getAvatar())
                .addGender(userBO.getGender())
                .addBirthday(userBO.getBirthday())
                .addAddress(userBO.getAddress())
                .addVersion()
                .getExtras());
        Opt.ofNullable(userBO)
                .map(UserBO::getDesc)
                .ifPresent(this::setDesc);
        return this;
    }

    /****************** get *******************/

    public String getNickname() {
        return Opt.ofNullable(nickname)
                .orElse(username);
    }
}

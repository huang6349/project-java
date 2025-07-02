package org.huangyalong.modules.system.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.huangyalong.core.constants.RegexpConstants;
import org.myframework.base.request.BaseBO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import java.util.Date;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户信息-BO")
public class UserBO extends BaseBO<Long> {

    @NotBlank(message = "帐号不能为空")
    @Pattern(regexp = RegexpConstants.USERNAME, message = "错误的帐号格式")
    @Schema(description = "用户帐号", requiredMode = REQUIRED)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = RegexpConstants.PASSWORD, message = "错误的密码格式")
    @Schema(description = "用户密码", requiredMode = REQUIRED)
    private String password1;

    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = RegexpConstants.PASSWORD, message = "错误的密码格式")
    @Schema(description = "确认密码", requiredMode = REQUIRED)
    private String password2;

    @NotBlank(message = "昵称不能为空")
    @Schema(description = "用户昵称")
    private String nickname;

    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = RegexpConstants.MOBILE, message = "错误的手机号码格式")
    @Schema(description = "手机号码", requiredMode = REQUIRED)
    private String mobile;

    @Email(message = "错误的邮箱格式")
    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户性别")
    private String gender;

    @PastOrPresent(message = "错误的生日格式")
    @Schema(description = "用户生日")
    private Date birthday;

    @Schema(description = "用户地址")
    private String address;

    @Schema(description = "备注")
    private String desc;
}

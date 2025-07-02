package org.huangyalong.modules.system.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huangyalong.modules.system.enums.UserGender;
import org.myframework.base.domain.BaseExtras;

import java.util.Date;

import static org.myframework.extra.dict.EnumDict.fromValue;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserExtras extends BaseExtras<UserExtras> {

    public static final String NAME_NICKNAME = "nickname";

    public static final String NAME_AVATAR = "avatar";

    public static final String NAME_GENDER = "gender";

    public static final String NAME_BIRTHDAY = "birthday";

    public static final String NAME_ADDRESS = "address";

    public static final String NAME_VERSION = "version";

    public static final String VERSION = "1.0.0";

    public UserExtras addNickname(String value) {
        add(NAME_NICKNAME, value);
        return self();
    }

    public UserExtras addAvatar(String value) {
        add(NAME_AVATAR, value);
        return self();
    }

    public UserExtras addGender(String value) {
        var gender = fromValue(value, UserGender.class);
        add(NAME_GENDER, Opt.ofNullable(gender)
                .map(UserGender::getValue)
                .orElse(null));
        return self();
    }

    public UserExtras addBirthday(Date value) {
        add(NAME_BIRTHDAY, Opt.ofNullable(value)
                .map(DateUtil::formatDate)
                .orElse(null));
        return self();
    }

    public UserExtras addAddress(String value) {
        add(NAME_ADDRESS, value);
        return self();
    }

    public UserExtras addVersion() {
        add(NAME_VERSION, VERSION);
        return self();
    }
}

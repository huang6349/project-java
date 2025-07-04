package org.huangyalong.core.constants;

public interface RegexpConstants {

    String USERNAME = "^[a-zA-Z]\\w{4,15}$";

    String PASSWORD = "^(?=.*[a-z])(?=.*[0-9])[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{6,18}$";

    String MOBILE = "^(?:(?:\\+|00)86)?1\\d{10}$";

    String CODE = "^[A-Za-z0-9@:._-]+$";
}

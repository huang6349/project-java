package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryChain;
import org.huangyalong.modules.system.domain.User;
import org.myframework.core.helper.FetchLoadHelper;

import java.io.Serializable;

import static org.huangyalong.modules.system.domain.UserExtras.NAME_NICKNAME;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.mybatisflex.JsonMethods.ue;

public class NicknameHelper extends FetchLoadHelper<String> {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile NicknameHelper instance;

    public static NicknameHelper getInstance() {
        if (!initialized) {
            synchronized (NicknameHelper.class) {
                if (!initialized) {
                    instance = new NicknameHelper();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @Override
    protected String fetch(Serializable id) {
        if (ObjectUtil.isNotEmpty(id)) {
            var username = QueryChain.of(User.class)
                    .select(USER.USERNAME)
                    .where(USER.ID.eq(id))
                    .oneAs(String.class);
            return QueryChain.of(User.class)
                    .select(ue(USER.EXTRAS, NAME_NICKNAME).as(User::getNickname))
                    .where(USER.ID.eq(id))
                    .oneAsOpt(String.class)
                    .orElse(username);
        } else return null;
    }

    /**
     * 根据用户编号获取昵称
     *
     * @param id 用户编号
     * @return 昵称
     */
    public static String getNickname(Object id) {
        if (ObjectUtil.isNotNull(id)) {
            var sId = (Serializable) id;
            return getInstance().get(sId);
        } else return null;
    }

    /**
     * 加载指定用户的昵称到缓存
     *
     * @param id 用户编号
     */
    public static void load(Object id) {
        if (ObjectUtil.isNull(id)) return;
        var sId = (Serializable) id;
        getInstance().load(sId);
    }
}

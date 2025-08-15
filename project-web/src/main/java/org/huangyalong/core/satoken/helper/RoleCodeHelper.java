package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.redis.RedisHelper;

import java.io.Serializable;
import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static cn.hutool.core.convert.Convert.toLong;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.core.constants.Constants.SUPER_ADMIN_ID;

public final class RoleCodeHelper {

    public static List<String> load(Serializable tenantId,
                                    Serializable id) {
        if (ObjectUtil.isNotEmpty(tenantId) && ObjectUtil.isNotNull(id)) {
            var roleService = SpringUtil.getBean(UserRoleService.class);
            return runBlock(roleService.list(tenantId, id)
                    .map(Role::getCode)
                    .filter(ObjectUtil::isNotEmpty)
                    .collectList());
        } else return empty();
    }

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_role_code_{}", message);
            RedisHelper.delete(key);
            var id = (Serializable) message;
            var roles = load(UserHelper.getTenant(), id);
            if (ObjectUtil.equal(SUPER_ADMIN_ID, toLong(message)))
                RedisHelper.lLeftPushAll(key, "super-admin");
            if (ObjectUtil.isNotEmpty(roles))
                RedisHelper.lLeftPushAll(key, roles);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

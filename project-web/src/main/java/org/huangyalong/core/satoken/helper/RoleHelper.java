package org.huangyalong.core.satoken.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.service.UserRoleService;
import org.myframework.core.redis.RedisHelper;

import java.io.Serializable;
import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static java.util.concurrent.TimeUnit.MINUTES;

public final class RoleHelper {

    public static List<String> load(Serializable tenantId,
                                    Serializable id) {
        if (ObjectUtil.isNotEmpty(tenantId) && ObjectUtil.isNotNull(id)) {
            return runBlock(SpringUtil.getBean(UserRoleService.class)
                    .list(tenantId, id)
                    .map(Role::getId)
                    .filter(ObjectUtil::isNotNull)
                    .map(Convert::toStr)
                    .collectList());
        } else return empty();
    }

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_role_{}", message);
            RedisHelper.delete(key);
            var id = (Serializable) message;
            var roles = load(UserHelper.getTenant(), id);
            if (ObjectUtil.isNotEmpty(roles))
                RedisHelper.lLeftPushAll(key, roles);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

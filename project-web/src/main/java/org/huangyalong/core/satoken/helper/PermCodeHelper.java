package org.huangyalong.core.satoken.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.service.UserPermService;
import org.myframework.core.redis.RedisHelper;

import java.io.Serializable;
import java.util.List;

import static cn.hutool.core.collection.ListUtil.empty;
import static cn.hutool.core.convert.Convert.toLong;
import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.huangyalong.core.constants.Constants.SUPER_ADMIN_ID;

public final class PermCodeHelper {

    public static List<String> load(Serializable tenantId,
                                    Serializable id) {
        if (ObjectUtil.isNotEmpty(tenantId) && ObjectUtil.isNotNull(id)) {
            var permService = SpringUtil.getBean(UserPermService.class);
            return runBlock(permService.all(tenantId, id)
                    .map(Perm::getCode)
                    .filter(ObjectUtil::isNotEmpty)
                    .collectList());
        } else return empty();
    }

    public static void load(Object message) {
        if (ObjectUtil.isNotEmpty(message)) {
            var key = format("user_perm_code_{}", message);
            RedisHelper.delete(key);
            var id = (Serializable) message;
            var perms = load(UserHelper.getTenant(), id);
            if (ObjectUtil.equal(SUPER_ADMIN_ID, toLong(message)))
                RedisHelper.lLeftPushAll(key, "*");
            if (ObjectUtil.isNotEmpty(perms))
                RedisHelper.lLeftPushAll(key, perms);
            RedisHelper.expire(key, 1, MINUTES);
        }
    }
}

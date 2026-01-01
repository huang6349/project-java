package org.huangyalong.core.mybatisflex;

import cn.hutool.core.convert.Convert;
import com.mybatisflex.core.tenant.TenantFactory;
import lombok.Getter;
import org.huangyalong.modules.system.properties.TenantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.hutool.core.lang.Opt.ofNullable;
import static cn.hutool.core.util.BooleanUtil.isTrue;
import static org.huangyalong.core.constants.TenantConstants.INVALID;
import static org.huangyalong.core.satoken.helper.UserHelper.getTenant;

@Getter
@Service
public class TenantFactoryImpl implements TenantFactory {

    private static final Long[] EMPTY = new Long[]{INVALID};

    @Autowired
    private TenantProperties properties;

    @Override
    public Object[] getTenantIds() {
        var enabled = ofNullable(getProperties())
                .map(TenantProperties::isEnabled)
                .orElse(Boolean.TRUE);
        if (isTrue(enabled)) {
            return ofNullable(getTenant())
                    .map(Convert::toLongArray)
                    .orElse(EMPTY);
        } else return null;
    }
}

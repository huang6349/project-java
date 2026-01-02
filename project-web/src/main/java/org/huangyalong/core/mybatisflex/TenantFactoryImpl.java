package org.huangyalong.core.mybatisflex;

import cn.hutool.core.convert.Convert;
import com.mybatisflex.core.tenant.TenantFactory;
import lombok.Getter;
import org.springframework.stereotype.Service;

import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.core.constants.TenantConstants.INVALID;
import static org.huangyalong.core.satoken.helper.SystemHelper.isTenantEnabled;
import static org.huangyalong.core.satoken.helper.UserHelper.getTenant;

@Getter
@Service
public class TenantFactoryImpl implements TenantFactory {

    private static final Long[] EMPTY = new Long[]{INVALID};

    @Override
    public Object[] getTenantIds() {
        if (isTenantEnabled()) {
            return ofNullable(getTenant())
                    .map(Convert::toLongArray)
                    .orElse(EMPTY);
        } else return null;
    }
}

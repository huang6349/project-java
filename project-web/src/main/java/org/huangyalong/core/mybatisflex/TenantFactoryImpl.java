package org.huangyalong.core.mybatisflex;

import cn.hutool.core.convert.Convert;
import com.mybatisflex.core.tenant.TenantFactory;
import lombok.Getter;
import org.springframework.stereotype.Service;

import static cn.hutool.core.convert.Convert.toLongArray;
import static cn.hutool.core.lang.Opt.ofNullable;
import static org.huangyalong.core.constants.TenantConstants.NONE;
import static org.huangyalong.core.constants.TenantConstants.INVALID;
import static org.huangyalong.core.satoken.helper.SystemHelper.allowTenant;
import static org.huangyalong.core.satoken.helper.UserHelper.getTenant;

@Getter
@Service
public class TenantFactoryImpl implements TenantFactory {

    @Override
    public Object[] getTenantIds() {
        if (allowTenant()) {
            return ofNullable(getTenant())
                    .map(Convert::toLongArray)
                    .orElse(toLongArray(INVALID));
        } else return toLongArray(NONE);
    }
}

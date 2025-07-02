package org.huangyalong.core.mybatisflex;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import com.mybatisflex.core.tenant.TenantFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static org.huangyalong.core.satoken.helper.UserHelper.getTenant;

@AllArgsConstructor
@Service
public class TenantFactoryImpl implements TenantFactory {

    @Override
    public Object[] getTenantIds() {
        return Opt.ofNullable(getTenant())
                .map(Convert::toLongArray)
                .orElse(new Long[]{0L});
    }
}

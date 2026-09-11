package org.huangyalong.modules.system.request;

import cn.hutool.core.lang.Opt;
import org.huangyalong.modules.system.enums.ConfigRule;

import static cn.hutool.core.util.ObjectUtil.equal;
import static org.huangyalong.core.constants.SystemConstants.CODE_RULES;

public interface SystemUtil {

    /**
     * 配置域是否只读：期望值由规则表派生，改规则无需改测试
     */
    static Boolean isReadonly(String code) {
        var rule = Opt.ofNullable(code)
                .map(CODE_RULES::get)
                .get();
        return equal(ConfigRule.RO, rule);
    }
}

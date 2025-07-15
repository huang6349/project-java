package org.huangyalong.core.satoken.stp;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import lombok.AllArgsConstructor;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StpInterfaceImpl implements StpInterface {

    private final static List<String> EMPTY = ListUtil.empty();

    @Override
    public List<String> getPermissionList(Object loginId,
                                          String loginType) {
        return Opt.ofBlankAble(loginId)
                .map(UserHelper::getPermCode)
                .orElse(EMPTY);
    }

    @Override
    public List<String> getRoleList(Object loginId,
                                    String loginType) {
        return Opt.ofBlankAble(loginId)
                .map(UserHelper::getRoleCode)
                .orElse(EMPTY);
    }
}

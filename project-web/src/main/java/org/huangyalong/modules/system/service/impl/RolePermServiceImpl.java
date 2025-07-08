package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.RoleAssoc;
import org.huangyalong.modules.system.mapper.RoleAssocMapper;
import org.huangyalong.modules.system.service.RolePermService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RolePermServiceImpl extends ReactorServiceImpl<RoleAssocMapper, RoleAssoc> implements RolePermService {
}

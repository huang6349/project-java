package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.mapper.RoleMapper;
import org.huangyalong.modules.system.service.RoleService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleServiceImpl extends ReactorServiceImpl<RoleMapper, Role> implements RoleService {
}

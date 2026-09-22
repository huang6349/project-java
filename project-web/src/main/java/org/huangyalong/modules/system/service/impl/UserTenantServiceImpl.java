package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.service.UserTenantService;
import org.springframework.stereotype.Service;

@Service
public class UserTenantServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserTenantService {
}

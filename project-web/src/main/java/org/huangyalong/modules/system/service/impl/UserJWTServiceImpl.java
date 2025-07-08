package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.service.UserJWTService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserJWTServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserJWTService {
}

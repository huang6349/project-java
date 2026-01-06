package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.Getter;
import org.huangyalong.modules.system.domain.System;
import org.huangyalong.modules.system.mapper.SystemMapper;
import org.huangyalong.modules.system.service.SystemService;
import org.springframework.stereotype.Service;

@Getter
@Service
public class SystemServiceImpl extends ReactorServiceImpl<SystemMapper, System> implements SystemService {
}

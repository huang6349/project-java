package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.Perm;
import org.huangyalong.modules.system.mapper.PermMapper;
import org.huangyalong.modules.system.service.PermService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PermServiceImpl extends ReactorServiceImpl<PermMapper, Perm> implements PermService {
}

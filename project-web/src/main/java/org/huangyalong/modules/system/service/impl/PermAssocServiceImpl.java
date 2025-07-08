package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.system.domain.PermAssoc;
import org.huangyalong.modules.system.mapper.PermAssocMapper;
import org.huangyalong.modules.system.service.PermAssocService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PermAssocServiceImpl extends ReactorServiceImpl<PermAssocMapper, PermAssoc> implements PermAssocService {
}

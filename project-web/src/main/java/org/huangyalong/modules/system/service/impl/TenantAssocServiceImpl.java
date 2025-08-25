package org.huangyalong.modules.system.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.modules.system.domain.TenantAssoc;
import org.huangyalong.modules.system.mapper.TenantAssocMapper;
import org.huangyalong.modules.system.service.TenantAssocService;
import org.springframework.stereotype.Service;

@Service
public class TenantAssocServiceImpl extends ReactorServiceImpl<TenantAssocMapper, TenantAssoc> implements TenantAssocService {
}

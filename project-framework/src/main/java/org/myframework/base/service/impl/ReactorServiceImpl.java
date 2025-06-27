package org.myframework.base.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.service.IService;
import lombok.Getter;
import org.myframework.base.service.ReactorService;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class ReactorServiceImpl<Mapper extends BaseMapper<Entity>, Entity>
        implements ReactorService<Entity> {

    @Autowired
    @Getter
    protected Mapper mapper;

    @Autowired(required = false)
    protected IService<Entity> blockService;

    @Override
    public IService<Entity> getBlockService() {
        if (ObjectUtil.isNull(blockService))
            blockService = ReactorServiceImpl.this::getMapper;
        return blockService;
    }
}

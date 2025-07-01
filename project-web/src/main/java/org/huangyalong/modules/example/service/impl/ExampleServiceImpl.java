package org.huangyalong.modules.example.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.example.domain.Example;
import org.huangyalong.modules.example.mapper.ExampleMapper;
import org.huangyalong.modules.example.request.ExampleBO;
import org.huangyalong.modules.example.service.ExampleService;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;
import static org.myframework.core.util.ServiceUtil.randomCode;

@AllArgsConstructor
@Service
public class ExampleServiceImpl extends ReactorServiceImpl<ExampleMapper, Example> implements ExampleService {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(ExampleBO exampleBO) {
        var data = Example.create()
                .setCode(randomCode())
                .with(exampleBO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(ExampleBO exampleBO) {
        var id = Opt.ofNullable(exampleBO)
                .map(ExampleBO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with(exampleBO);
        return updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data);
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }
}

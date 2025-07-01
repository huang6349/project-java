package org.huangyalong.modules.example.service;

import com.mybatis.flex.reactor.core.ReactorService;
import org.huangyalong.modules.example.domain.Example;
import org.huangyalong.modules.example.request.ExampleBO;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface ExampleService extends ReactorService<Example> {

    Mono<Boolean> add(ExampleBO exampleBO);

    Mono<Boolean> update(ExampleBO exampleBO);

    Mono<Boolean> delete(Serializable id);
}

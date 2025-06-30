package org.huangyalong.modules.example.service;

import org.huangyalong.modules.example.domain.Example;
import org.huangyalong.modules.example.request.ExampleBO;
import org.myframework.base.service.ReactorService;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface ExampleService extends ReactorService<Example> {

    Mono<Boolean> add(ExampleBO exampleBO);

    Mono<Boolean> update(ExampleBO exampleBO);

    Mono<Boolean> delete(Serializable id);
}

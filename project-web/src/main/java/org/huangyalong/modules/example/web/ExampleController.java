package org.huangyalong.modules.example.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.example.domain.Example;
import org.huangyalong.modules.example.request.ExampleBO;
import org.huangyalong.modules.example.request.ExampleQueries;
import org.huangyalong.modules.example.service.ExampleService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@example")
@RestController
@RequestMapping("/example")
@Tag(name = "示例管理")
public class ExampleController extends ReactorController<
        ExampleService,
        Long,
        Example,
        ExampleQueries,
        ExampleBO,
        ExampleBO
        > {

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(ExampleBO exampleBO) {
        var data = getReactorService()
                .add(exampleBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(ExampleBO exampleBO) {
        var data = getReactorService()
                .update(exampleBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getReactorService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

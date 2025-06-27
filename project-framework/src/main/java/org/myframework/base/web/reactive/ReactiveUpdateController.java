package org.myframework.base.web.reactive;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface ReactiveUpdateController<Entity, UpdateBO> extends ReactiveBaseController<Entity> {

    @PutMapping
    @Operation(summary = "修改")
    default Mono<Boolean> update(@RequestBody @Validated UpdateBO updateBO) {
        var result = handlerUpdate(updateBO);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return result.getData();
        // UpdateBO -> Entity
        var entity = BeanUtil.toBean(updateBO, getEntityClass());
        return getReactorService()
                .updateById(entity);
    }

    default ApiResponse<Mono<Boolean>> handlerUpdate(UpdateBO updateBO) {
        StaticLog.trace("自定义修改: {}", updateBO);
        return ApiResponse.okDef();
    }
}

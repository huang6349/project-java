package org.myframework.base.web.reactive;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.validation.Save;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface ReactiveSaveController<Entity, SaveBO> extends ReactiveBaseController<Entity> {

    @PostMapping
    @Operation(summary = "新增")
    default Mono<Boolean> save(@RequestBody @Validated(Save.class) SaveBO saveBO) {
        var result = handlerSave(saveBO);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return result.getData();
        // SaveBO -> Entity
        var entity = BeanUtil.toBean(saveBO, getEntityClass());
        return getReactorService()
                .save(entity);
    }

    default ApiResponse<Mono<Boolean>> handlerSave(SaveBO saveBO) {
        StaticLog.trace("自定义新增: {}", saveBO);
        return ApiResponse.okDef();
    }
}

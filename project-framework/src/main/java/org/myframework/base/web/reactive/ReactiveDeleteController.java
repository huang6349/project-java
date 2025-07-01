package org.myframework.base.web.reactive;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface ReactiveDeleteController<Entity, Id extends Serializable> extends ReactiveBaseController<Entity> {

    @PreCheckPermission(value = {"{}:delete", "{}:remove"}, mode = PreMode.OR)
    @DeleteMapping("/{id:.+}")
    @Operation(summary = "单体删除")
    default Mono<Boolean> delete(@PathVariable Id id) {
        var result = handlerDelete(id);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return result.getData();
        return getReactorService()
                .removeById(id);
    }

    default ApiResponse<Mono<Boolean>> handlerDelete(Id id) {
        StaticLog.trace("自定义单体删除: {}", id);
        return ApiResponse.okDef();
    }
}

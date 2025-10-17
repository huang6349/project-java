package org.myframework.base.web.curd;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.BaseController;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface DeleteController<Entity, Id extends Serializable> extends BaseController<Entity> {

    @PreCheckPermission(value = {"{}:delete", "{}:remove"}, mode = PreMode.OR)
    @DeleteMapping("/{id:.+}")
    @Operation(summary = "根据主键删除")
    default Mono<Boolean> delete(@PathVariable Id id) {
        var result = handlerDelete(id);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return result.getData();
        return getBaseService()
                .removeById(id);
    }

    default ApiResponse<Mono<Boolean>> handlerDelete(Id id) {
        StaticLog.trace("自定义单体删除: {}", id);
        return ApiResponse.okDef();
    }
}

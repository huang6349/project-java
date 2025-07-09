package org.myframework.base.web.reactive;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.validation.Update;
import org.myframework.base.web.BaseController;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface UpdateController<Entity, UpdateBO> extends BaseController<Entity> {

    @PreCheckPermission(value = {"{}:edit", "{}:update"}, mode = PreMode.OR)
    @PutMapping
    @Operation(summary = "修改")
    default Mono<Boolean> update(@RequestBody @Validated(Update.class) UpdateBO updateBO) {
        var result = handlerUpdate(updateBO);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return result.getData();
        // UpdateBO -> Entity
        var entity = BeanUtil.toBean(updateBO, getEntityClass());
        return getBaseService()
                .updateById(entity);
    }

    default ApiResponse<Mono<Boolean>> handlerUpdate(UpdateBO updateBO) {
        StaticLog.trace("自定义修改: {}", updateBO);
        return ApiResponse.okDef();
    }
}

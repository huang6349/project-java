package org.myframework.base.web.reactive;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.log.StaticLog;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.validation.Save;
import org.myframework.base.web.BaseController;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface SaveController<Entity, SaveBO> extends BaseController<Entity> {

    @PreCheckPermission(value = {"{}:add", "{}:save"}, mode = PreMode.OR)
    @PostMapping
    @Operation(summary = "新增")
    default Mono<Boolean> save(@RequestBody @Validated(Save.class) SaveBO saveBO) {
        var result = handlerSave(saveBO);
        if (BooleanUtil.isFalse(result.getDefExec()))
            return result.getData();
        // SaveBO -> Entity
        var entity = BeanUtil.toBean(saveBO, getEntityClass());
        return getBaseService()
                .save(entity);
    }

    default ApiResponse<Mono<Boolean>> handlerSave(SaveBO saveBO) {
        StaticLog.trace("自定义新增: {}", saveBO);
        return ApiResponse.okDef();
    }
}

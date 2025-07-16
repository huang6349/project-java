package org.myframework.base.web.extra;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.util.BooleanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.response.OptionVO;
import org.myframework.base.web.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

public interface OptionController<Entity, Queries> extends BaseController<Entity> {

    @SaCheckLogin
    @GetMapping("/_items")
    @Operation(summary = "选项查询")
    default Flux<OptionVO> items(Queries queries) {
        var result = handlerOption(queries);
        var query = result.getData();
        if (BooleanUtil.isFalse(result.getDefExec()))
            return getBaseService()
                    .listAs(query, OptionVO.class);
        return Flux.empty();
    }

    ApiResponse<QueryWrapper> handlerOption(Queries queries);
}

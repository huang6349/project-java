package org.huangyalong.modules.notify.web;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.notify.domain.NotifyCategory;
import org.huangyalong.modules.notify.request.CategoryBO;
import org.huangyalong.modules.notify.request.CategoryQueries;
import org.huangyalong.modules.notify.service.NotifyCategoryService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.base.web.extra.OptionController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@notify")
@RestController
@RequestMapping("/notify/category")
@Tag(name = "消息类别")
public class NotifyCategoryController extends ReactorController<
        NotifyCategoryService,
        Long,
        NotifyCategory,
        CategoryQueries,
        CategoryBO,
        CategoryBO
        > implements OptionController<
        NotifyCategory,
        CategoryQueries> {

    @Override
    public ApiResponse<QueryWrapper> handlerOption(CategoryQueries queries) {
        var data = getBaseService()
                .getOptionWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(CategoryQueries queries) {
        var data = getBaseService().
                getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave(CategoryBO categoryBO) {
        var data = getBaseService()
                .add(categoryBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate(CategoryBO categoryBO) {
        var data = getBaseService()
                .update(categoryBO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}

package org.huangyalong.modules.system.web;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreMode;
import org.myframework.extra.dict.DictCache;
import org.myframework.extra.dict.DictDefine;
import org.myframework.extra.dict.ItemDefine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@PreAuth(replace = "@dict")
@RestController
@RequestMapping("/dict")
@Tag(name = "字典管理")
public class DictController {

    @GetMapping("/{category:.+}/_items")
    @Operation(summary = "选项查询")
    public Mono<List<ItemDefine>> items(@PathVariable String category) {
        var define = DictCache.query(category);
        var items = Opt.ofNullable(define)
                .map(DictDefine::getItems)
                .get();
        return Mono.justOrEmpty(items);
    }

    @PreCheckPermission(value = {"{}:query", "{}:view"}, mode = PreMode.OR)
    @GetMapping("/{category:.+}")
    @Operation(summary = "单体查询")
    public Mono<DictDefine> query(@PathVariable String category) {
        var define = DictCache.query(category);
        return Mono.justOrEmpty(define);
    }
}

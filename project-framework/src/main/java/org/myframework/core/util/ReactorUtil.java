package org.myframework.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.mybatis.flex.reactor.core.utils.ReactorUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public final class ReactorUtil extends ReactorUtils {

    public static <T> Flux<T> toFlux(Supplier<Iterable<? extends T>> supplier) {
        return Mono.fromSupplier(supplier)
                .flatMapMany(iterable -> ObjectUtil.isNotNull(iterable)
                        ? Flux.fromIterable(iterable)
                        : Flux.empty());
    }
}

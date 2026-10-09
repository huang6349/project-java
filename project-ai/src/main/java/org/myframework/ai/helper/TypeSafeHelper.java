package org.myframework.ai.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.log.StaticLog;
import io.github.premocloud.typesafe.*;
import org.myframework.core.exception.BusinessException;
import org.myframework.extra.dict.EnumDict;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

/**
 * TypeSafe 判定入口，三档由细到粗：{@link #buildAsk} 只建题，
 * {@link #answer} 调判定服务拿带置信度的答案，{@link #ask} 一步拿到筛过概率的常量；
 * 异步等进阶能力再用 {@link #getClient()}。
 *
 * <p>失败一律抛 {@link BusinessException}，需要降级回退的调用方自行 catch。</p>
 */
public class TypeSafeHelper extends AbstractTypeSafeHelper {

    /**
     * 单次判定约 2-8s，超时即失败；同步调用不重试，免得超时后重复发起
     */
    private static final RequestOptions OPTIONS = RequestOptions.of(options -> options
            .timeout(Duration.ofSeconds(10))
            .maxRetries(0));

    /**
     * 「接近」的判定比例：概率达到最高概率的该比例即算接近
     */
    private static final double CLOSE_RATIO = 0.8;

    /**
     * 按枚举常量建一道分类题，选项描述由调用方给出——
     * 需要喂判据而非枚举标签时用这个
     *
     * @param key          题名，请求与响应按它取答案，沿用既有题名可避免无谓的行为变更
     * @param instructions 分类指令
     * @param type         实现 {@link EnumDict} 且至少有一个常量
     * @param describer    每个常量的选项描述
     * @throws BusinessException {@code type} 没有常量
     */
    public static <E extends Enum<E> & EnumDict<?>> ChoiceAsk<E> buildAsk(
            String key,
            String instructions,
            Class<E> type,
            Function<E, String> describer) {
        validate(type);
        return Ask.choice(key, type, builder -> {
            builder.instructions(instructions);
            for (var constant : type.getEnumConstants()) {
                builder.option(constant, describer.apply(constant));
            }
        });
    }

    /**
     * 按枚举常量建分类题，选项描述默认取 {@link EnumDict#getLabel()}
     *
     * @param key          题名，请求与响应按它取答案
     * @param instructions 分类指令
     * @param type         实现 {@link EnumDict} 且至少有一个常量
     * @throws BusinessException {@code type} 没有常量
     */
    public static <E extends Enum<E> & EnumDict<?>> ChoiceAsk<E> buildAsk(
            String key,
            String instructions,
            Class<E> type) {
        return buildAsk(key, instructions, type, EnumDict::getLabel);
    }

    /**
     * 向判定服务发起一次分类，取回带置信度的完整答案
     *
     * @param state 任意可被 Jackson 序列化的对象
     * @param ask   建好的分类题
     * @param <E>   枚举类型
     * @return 判定答案，含 choice / confidence / probabilities
     * @throws BusinessException 未配置 {@code typesafe.api-key}；超时等服务不可用原样抛出
     */
    public static <E> ChoiceAnswer<E> answer(
            Object state,
            Ask<ChoiceAnswer<E>> ask) {
        var request = TypeSafeRequest.of(state, ask);
        return getClient()
                .systemOne(request, OPTIONS)
                .answer(ask);
    }

    /**
     * 把 {@code state} 归类到 {@code type} 的某个常量，返回接近最高概率的常量及其概率
     *
     * <p>选项标签用常量 {@link Enum#name()}（SDK 枚举路径强制），
     * {@link EnumDict#getLabel()} 作描述喂给模型——枚举名多是 {@code TYPE0} 这种无语义标识。</p>
     *
     * @param state        任意可被 Jackson 序列化的对象
     * @param instructions 分类指令，说明要把 state 判成哪个维度
     * @param type         实现 {@link EnumDict} 且至少有一个常量
     * @return 接近最高概率的常量 → 概率（0~1），降序；并列保持传入顺序
     * @throws BusinessException 未配置 {@code typesafe.api-key}，或 {@code type} 没有常量
     */
    public static <E extends Enum<E> & EnumDict<?>> Map<E, Double> ask(
            Object state,
            String instructions,
            Class<E> type) {
        StaticLog.trace("发起 System One 分类提问: {}", instructions);
        var enumAsk = buildAsk(type.getSimpleName(), instructions, type);
        return filterCloseToMax(answer(state, enumAsk).probabilities());
    }

    /**
     * 筛出概率不低于最高概率 {@value #CLOSE_RATIO} 倍的项，降序；
     * 并列保持传入顺序，概率表为空时返回空 Map
     */
    static <E> Map<E, Double> filterCloseToMax(Map<E, Double> probabilities) {
        if (CollUtil.isEmpty(probabilities)) return Map.of();
        var threshold = CollUtil.max(probabilities.values()) * CLOSE_RATIO;
        var sorted = MapUtil.sortByValue(probabilities, Boolean.TRUE);
        return MapUtil.filter(sorted, entry -> entry.getValue() >= threshold);
    }

    /**
     * 建题前校验枚举有常量，否则选项为空，SDK 会抛内部异常
     */
    private static <E extends Enum<E> & EnumDict<?>> void validate(Class<E> type) {
        if (type.getEnumConstants().length > 0) return;
        throw new BusinessException("枚举没有常量");
    }
}

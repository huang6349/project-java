package org.myframework.ai.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.log.StaticLog;
import io.github.premocloud.typesafe.Ask;
import io.github.premocloud.typesafe.ChoiceAsk;
import org.myframework.core.exception.BusinessException;
import org.myframework.extra.dict.EnumDict;

import java.util.Map;

/**
 * TypeSafe 助手
 *
 * <p>业务代码通过 {@link #getClient()} 静态取得 {@link io.github.premocloud.typesafe.TypeSafeClient}，
 * 再自行调用 {@code systemOne(...)} / {@code systemOneAsync(...)} 等 SDK 方法；
 * {@link #ask} 则把「把状态归类到某个枚举」这一常见场景封装成一次调用。</p>
 *
 * @see AbstractTypeSafeHelper#getClient()
 */
public class TypeSafeHelper extends AbstractTypeSafeHelper {

    /**
     * 「接近」的判定比例：概率不低于最高概率的该比例即视为接近，并列时可返回多个
     */
    private static final double CLOSE_RATIO = 0.8;

    /**
     * 把 {@code state} 归类到 {@code type} 的某个常量，返回接近最高概率的常量及其概率
     *
     * <p>以常量 {@link Enum#name()} 作为选项标签（SDK 的枚举路径强制如此），
     * {@link EnumDict#getLabel()} 作为选项描述喂给模型 —— 本项目枚举名多为 {@code TYPE0} 这类无语义标识，
     * 含义靠描述传达。</p>
     *
     * @param state        提问所依赖的状态，任意可被 Jackson 序列化的对象
     * @param instructions 分类指令，说明要把 state 判成哪个维度
     * @param type         实现 {@link EnumDict} 的枚举类型，且至少有一个常量
     * @return 接近最高概率的常量 → 概率（0~1），按概率降序；概率并列时保持传入顺序
     * @throws BusinessException 未配置 {@code typesafe.api-key}，或 {@code type} 没有常量
     */
    public static <E extends Enum<E> & EnumDict<?>> Map<E, Double> ask(Object state,
                                                                       String instructions,
                                                                       Class<E> type) {
        StaticLog.trace("发起 System One 分类提问: {}", instructions);
        validate(type);
        var enumAsk = buildAsk(instructions, type);
        return filterCloseToMax(getClient()
                .systemOne(state, enumAsk)
                .answer(enumAsk)
                .probabilities());
    }

    /**
     * 筛出概率不低于最高概率 {@value #CLOSE_RATIO} 倍的项，按概率降序
     *
     * <p>相等的概率保持传入顺序（排序与过滤都是稳定转换）；空输入返回空 Map。</p>
     */
    static <E> Map<E, Double> filterCloseToMax(Map<E, Double> probabilities) {
        var maxProbability = Opt.ofNullable(probabilities)
                .map(Map::values)
                .map(CollUtil::max)
                .orElse(0.0);
        var threshold = maxProbability * CLOSE_RATIO;
        var sorted = MapUtil.sortByValue(probabilities, Boolean.TRUE);
        return MapUtil.filter(sorted, entry -> {
            var probability = entry.getValue();
            return probability >= threshold;
        });
    }

    /**
     * 校验枚举至少有一个常量，否则选项为空，SDK 会在建题时抛内部异常
     */
    private static <E extends Enum<E> & EnumDict<?>> void validate(Class<E> type) {
        if (type.getEnumConstants().length > 0) return;
        throw new BusinessException("枚举没有常量");
    }

    /**
     * 按枚举常量建一道分类题：常量名作选项标签，{@link EnumDict#getLabel()} 作选项描述
     */
    private static <E extends Enum<E> & EnumDict<?>> ChoiceAsk<E> buildAsk(String instructions,
                                                                           Class<E> type) {
        return Ask.choice(type.getSimpleName(), type, builder -> {
            builder.instructions(instructions);
            for (var constant : type.getEnumConstants()) {
                builder.option(constant, constant.getLabel());
            }
        });
    }
}

package org.myframework.iot;

import cn.hutool.core.util.ObjectUtil;
import com.iteaj.iot.FrameworkComponent;
import com.iteaj.iot.FrameworkManager;
import lombok.Getter;

/**
 * 静态协议供应商
 *
 * <p>以懒加载方式创建并注册协议组件：首次调用 {@link #createComponent()} 时
 * 通过 {@link #doCreateComponent()} 创建实例，并自动调用
 * {@link FrameworkManager#register(FrameworkComponent)} 注册到框架，
 * 后续调用直接返回同一实例（幂等）。</p>
 *
 * <p>内部以 {@code volatile} 字段配合 {@code synchronized} 方法保证并发安全，
 * 具体供应商只需继承本类并实现 {@link #doCreateComponent()} 提供组件实例。</p>
 *
 * @param <T> 组件类型，须继承 {@link FrameworkComponent}
 */
public abstract class StaticProtocolSupplier<T extends FrameworkComponent>
        implements DeviceProtocolSupplier<T> {

    /**
     * 创建协议组件实例，由子类实现。
     *
     * @return 协议组件实例
     */
    protected abstract T doCreateComponent();

    @Getter
    private volatile T component;

    @Override
    public synchronized T createComponent() {
        if (ObjectUtil.isNotNull(component)) return component;
        component = doCreateComponent();
        FrameworkManager.register(component);
        return component;
    }
}

package org.myframework.core.satoken.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.log.StaticLog;
import org.myframework.core.satoken.util.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaTokenInterceptor extends SaInterceptor {

    @SuppressWarnings("all")
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // 清空 ThreadLocal 的值，防止下次请求时获取到的值是旧数据，同时也能防止内存溢出
        ContextUtil.remove();
    }

    @SuppressWarnings("all")
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        StaticLog.trace("本次请求的请求路径为: {}", request.getServletPath());
        return super.preHandle(request, response, handler);
    }

    public SaTokenInterceptor() {
        super(handler -> {
            // 设置 ThreadLocal 的值
            var loginId = StpUtil.getLoginIdDefaultNull();
            Opt.ofBlankAble(loginId)
                    .map(Convert::toLong)
                    .ifPresent(ContextUtil::setLoginId);
        });
    }
}

package org.myframework.core.satoken.aspect;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.core.satoken.annotation.PreCheckPermission;
import org.myframework.core.satoken.annotation.PreCheckRole;
import org.myframework.core.satoken.annotation.PreMode;
import org.myframework.core.satoken.properties.IgnoreProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.hutool.core.text.CharSequenceUtil.format;

@AllArgsConstructor
@Component
@Aspect
public class PreCheckAspect {

    private final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final String POINTCUT_SIGN =
            "@within(org.myframework.core.satoken.annotation.PreCheckPermission) || " +
                    "@annotation(org.myframework.core.satoken.annotation.PreCheckPermission) || " +
                    "@within(org.myframework.core.satoken.annotation.PreCheckRole) || " +
                    "@annotation(org.myframework.core.satoken.annotation.PreCheckRole)";

    private final IgnoreProperties ignoreProperties;

    @Around(POINTCUT_SIGN)
    Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();

        var replace = "";
        var enabled = false;

        // 获取父类上的@PreAuth注解
        var declaringClass = method.getDeclaringClass();
        if (declaringClass.isAnnotationPresent(PreAuth.class)) {
            var declaringAnnotation = declaringClass.getAnnotation(PreAuth.class);
            enabled = declaringAnnotation.enabled();
            replace = declaringAnnotation.replace();
        }

        // 获取类上面的@PreAuth注解
        var clazz = joinPoint.getTarget().getClass();
        if (clazz.isAnnotationPresent(PreAuth.class)) {
            var clazzAnnotation = clazz.getAnnotation(PreAuth.class);
            enabled = clazzAnnotation.enabled();
            replace = clazzAnnotation.replace();
        }

        // 判断路由是否放行路由
        if (isIgnoreToken())
            enabled = false;
        // 鉴权
        if (enabled)
            checkMethodAnnotation(replace, method);
        return joinPoint.proceed();
    }

    private void checkMethodAnnotation(String replace,
                                       Method method) {
        // 先校验 Method 所属 Class 上的注解
        validateAnnotation(replace, method.getDeclaringClass());
        // 再校验 Method 上的注解
        validateAnnotation(replace, method);
    }

    private void validateAnnotation(String replace,
                                    AnnotatedElement element) {
        var checkPermission = element.getAnnotation(PreCheckPermission.class);
        if (checkPermission != null && checkPermission.enabled())
            checkByAnnotation(replace, checkPermission);
        var checkRole = element.getAnnotation(PreCheckRole.class);
        if (checkRole != null && checkRole.enabled())
            checkByAnnotation(checkRole);
    }

    private void checkByAnnotation(String replace,
                                   PreCheckPermission annotation) {
        var value = annotation.value();
        value = handleValue(replace, value);
        try {
            if (annotation.mode() == PreMode.AND)
                StpUtil.checkPermissionAnd(value);
            else StpUtil.checkPermissionOr(value);
        } catch (NotPermissionException e) {
            // 权限认证未通过，再开始角色认证
            if (ArrayUtil.isNotEmpty(annotation.orRole())) {
                for (var role : annotation.orRole()) {
                    var roleArray = SaFoxUtil.convertStringToArray(role);
                    // 某一项role认证通过，则可以提前退出了，代表通过
                    if (StpUtil.hasRoleAnd(roleArray)) {
                        return;
                    }
                }
            }
            throw e;
        }
    }

    private void checkByAnnotation(PreCheckRole annotation) {
        var value = annotation.value();
        if (annotation.mode() == PreMode.AND)
            StpUtil.checkRoleAnd(value);
        else StpUtil.checkRoleOr(value);
    }

    private String[] handleValue(String replace,
                                 String[] value) {
        var result = value.clone();
        if (StrUtil.isNotBlank(replace))
            result = Arrays.stream(result)
                    .map(it -> format(it, replace))
                    .toList()
                    .toArray(new String[value.length]);
        return result;
    }

    private Boolean isIgnoreToken() {
        // 获取当前访问的路由。获取不到直接return false
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(attributes))
            return Boolean.FALSE;
        var request = attributes.getRequest();
        var path = request.getRequestURI();
        // 判断当前访问的路由，是否是saToken放行路由.
        return ignoreProperties
                .getNotMatchUrl()
                .stream()
                .anyMatch(url -> ANT_PATH_MATCHER.match(url, path));
    }
}

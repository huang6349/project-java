package org.myframework.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.lang.Opt;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.response.ShowType;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

public abstract class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(MethodArgumentNotValidException ex) {
        return ApiResponse.fail(
                Opt.ofNullable(ex)
                        .map(MethodArgumentNotValidException::getBindingResult)
                        .map(BindingResult::getFieldError)
                        .map(FieldError::getDefaultMessage)
                        .get(),
                ErrorCode.ERR_ARGUMENT.getCode(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(NoHandlerFoundException ex) {
        return ApiResponse.fail(
                "请检查请求路径或者类别是否正确",
                ErrorCode.NOT_FOUND.getCode(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(NotLoginException ex) {
        return ApiResponse.fail(
                switch (ex.getType()) {
                    case NotLoginException.NOT_TOKEN -> NotLoginException.NOT_TOKEN_MESSAGE;
                    case NotLoginException.INVALID_TOKEN -> NotLoginException.INVALID_TOKEN_MESSAGE;
                    case NotLoginException.TOKEN_TIMEOUT -> NotLoginException.TOKEN_TIMEOUT_MESSAGE;
                    case NotLoginException.BE_REPLACED -> NotLoginException.BE_REPLACED_MESSAGE;
                    case NotLoginException.KICK_OUT -> NotLoginException.KICK_OUT_MESSAGE;
                    default -> NotLoginException.DEFAULT_MESSAGE;
                },
                ErrorCode.UNAUTHORIZED.getCode(),
                ex.getClass().getName(),
                ShowType.REDIRECT
        );
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(NotPermissionException ex) {
        return ApiResponse.fail(
                ErrorCode.FORBIDDEN.getMessage(),
                ErrorCode.FORBIDDEN.getCode(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(NotRoleException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(NotRoleException ex) {
        return ApiResponse.fail(
                ErrorCode.FORBIDDEN.getMessage(),
                ErrorCode.FORBIDDEN.getCode(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(SaTokenException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(SaTokenException ex) {
        if (ex instanceof NotLoginException)
            return handleException(ex);
        if (ex instanceof NotPermissionException)
            return handleException(ex);
        if (ex instanceof NotRoleException)
            return handleException(ex);
        return ApiResponse.fail(
                ex.getMessage(),
                ErrorCode.FORBIDDEN.getCode(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(BusinessException ex) {
        return ApiResponse.fail(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getClass().getName(),
                ex.getShowType(),
                ex.getTraceId(),
                ex.getHost()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> handleException(Exception ex) {
        return ApiResponse.fail(
                ErrorCode.ERR_BUSINESS.getMessage(),
                ErrorCode.ERR_BUSINESS.getCode(),
                ex.getClass().getName()
        );
    }
}

package org.myframework.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /***************************通用错误******************************/
    OK(0, "操作成功"),
    SUCCESS(0, "操作成功"),
    ERROR(-1, "操作失败"),
    FAIL(-1, "操作失败"),
    SYSTEM_BUSY(1001, "系统繁忙"),

    /***************************请求错误******************************/
    BAD_REQUEST(400, "错误的请求"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "没有访问权限"),
    NOT_FOUND(404, "没有获取到数据"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求类型"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    BAD_GATEWAY(502, "网关错误"),
    ERR_BUSINESS(10000, "业务异常"),
    ERR_ARGUMENT(20000, "参数错误"),

    /***************************其它错误******************************/
    ERR_RESERVED(1001, "保留数据不允许删除");

    private final Integer code;

    private final String message;
}

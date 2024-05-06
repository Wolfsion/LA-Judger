package com.lavson.common.exception;

import com.lavson.common.norm.ErrorCode;

/**
 * 自定义的业务异常类
 *
 * @author LA
 * @version 1.0
 * 2024/5/6 - 21:10
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}

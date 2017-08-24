package com.rishiqing.mid.exception;

/**
 * 参数错误的exception
 * Created by  on 2017/7/31.Wallace
 */
public class MidUserMissingParamsException extends RuntimeException {
    public MidUserMissingParamsException() {
        super();
    }

    public MidUserMissingParamsException(String message) {
        super(message);
    }

    public MidUserMissingParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MidUserMissingParamsException(Throwable cause) {
        super(cause);
    }

    protected MidUserMissingParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

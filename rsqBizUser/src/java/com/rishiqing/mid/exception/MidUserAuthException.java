package com.rishiqing.mid.exception;

/**
 * 权限错误的exception
 * Created by  on 2017/7/31.Wallace
 */
public class MidUserAuthException extends RuntimeException {
    public MidUserAuthException() {
        super();
    }

    public MidUserAuthException(String message) {
        super(message);
    }

    public MidUserAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public MidUserAuthException(Throwable cause) {
        super(cause);
    }

    protected MidUserAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

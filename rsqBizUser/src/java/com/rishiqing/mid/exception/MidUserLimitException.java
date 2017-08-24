package com.rishiqing.mid.exception;

/**
 * 当进行敏感的批量操作时，为防止一次性操作的数据库的数据量过大，直接抛出此exception
 * Created by  on 2017/7/31.Wallace
 */
public class MidUserLimitException extends RuntimeException {
    public MidUserLimitException() {
        super();
    }

    public MidUserLimitException(String message) {
        super(message);
    }

    public MidUserLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public MidUserLimitException(Throwable cause) {
        super(cause);
    }

    protected MidUserLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

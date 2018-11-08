package com.lf.framework.core.exception;

/**
 * @Author: YuanYan
 * @Create At: 2018-03-22 15:19
 * @Description:自定义异常父类
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -2844495069670162381L;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BaseException(String message) {
        super(message);
    }
}

package com.lf.framework.core.exception;

/**
 * Author: YuanYan
 * Create At: 2018/3/22 15:33
 * Description:自定义系统异常
 */
public class SystemException extends BaseException {
    private static final long serialVersionUID = 1L;

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }
}

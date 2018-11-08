package com.lf.framework.core.exception;

/**
 * Author: YuanYan
 * Create At: 2018/3/22 15:23
 * Description:自定义用户Session超时失效异常
 */
public class SessionInvalidException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SessionInvalidException() {
		super();
	}

	public SessionInvalidException(String message) {
		super(message);
	}
}

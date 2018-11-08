package com.lf.framework.core.exception;

/**
 * Author: YuanYan
 * Create At: 2018/3/22 15:34
 * Description:自定义服务异常
 */
public class ServiceException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
}

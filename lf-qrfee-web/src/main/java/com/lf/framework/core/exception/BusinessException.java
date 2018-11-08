package com.lf.framework.core.exception;

/**
 * Author: YuanYan
 * Create At: 2018/3/22 15:23
 * Description:自定义业务异常
 */
public class BusinessException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(String message) {
		super(message);
	}
}

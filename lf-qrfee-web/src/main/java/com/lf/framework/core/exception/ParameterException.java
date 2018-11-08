package com.lf.framework.core.exception;

/**
 * Author: YuanYan
 * Create At: 2018/3/22 15:33
 * Description:自定义参数异常
 */
public class ParameterException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public ParameterException() {
		super();
	}
	
	public ParameterException(String message) {
		super(message);
	} 

	public ParameterException(String format, Exception e) {}

}

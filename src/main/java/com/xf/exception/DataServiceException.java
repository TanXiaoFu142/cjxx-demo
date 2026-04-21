package com.xf.exception;

/**
 * 业务错误基础类
 * 
 * @author joe.xie
 *
 */
public class DataServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataServiceException() {
		super();
	}

	public DataServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataServiceException(String message) {
		super(message);
	}

	public DataServiceException(Throwable cause) {
		super(cause);
	}

}
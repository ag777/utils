package com.ag777.util.lang.exception.model;

/**
 * 验证异常(验证参数过程，判断出的异常)
 * @author ag777
 *
 * @version create on 2018年05月17日,last modify at 2020年04月24日
 */
public class ValidateException extends Exception {

	private static final long serialVersionUID = -1358796764058245553L;

	private String extraMsg;	//拓展信息
	private Boolean isException;	// 可以用于确定是系统异常还是输入异常

	public String getExtraMsg() {
		return extraMsg;
	}

	public ValidateException(String message){
		super(message);
		this.isException = false;
	}

	public ValidateException(String message, String extraMsg){
		super(message);
		this.extraMsg = extraMsg;
		this.isException = false;
	}

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
		this.isException = cause != null;
	}

	public ValidateException(String message, String extraMsg, Throwable cause) {
		super(message, cause);
		this.extraMsg = extraMsg;
		this.isException = cause != null;
	}

	public Boolean getException() {
		return isException;
	}

	public ValidateException setException(Boolean exception) {
		isException = exception;
		return this;
	}

	public ValidateException setMessage(String newMessage) {
		return new ValidateException(newMessage, getCause());
	}

}

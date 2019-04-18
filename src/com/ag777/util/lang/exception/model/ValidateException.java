package com.ag777.util.lang.exception.model;

/**
 * 验证异常(验证参数过程，判断出的异常)
 * @author ag777
 *
 * @version create on 2018年05月17日,last modify at 2019年03月12日
 */
public class ValidateException extends Exception {

	private static final long serialVersionUID = -1358796764058245553L;

	private String extraMsg;	//拓展信息
	
	public String getExtraMsg() {
		return extraMsg;
	}
	
	public ValidateException(String message){
		super(message);
	}
	
	public ValidateException(String message, String extraMsg){
		super(message);
		this.extraMsg = extraMsg;
	}

}

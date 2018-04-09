package com.ag777.util.lang.exception.model;

/**
 * json语法异常，一般在json转换中使用
 * @author wanggz
 *
 */
public class JsonSyntaxException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 46516454273625416L;

	public JsonSyntaxException(Throwable throwable) {
		super(throwable);
	}
}

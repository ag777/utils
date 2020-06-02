package com.ag777.util.lang.exception.model;

/**
 * 除数为0异常
 * @author ag777
 *
 * @version create on 2018年07月19日,last modify at 2018年07月19日
 */
public class DivisorIsZeroException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 188844524372497557L;

	public DivisorIsZeroException() {
		super("除数不能为0");
	}
	
	public DivisorIsZeroException(String message) {
		super(message);
	}
}

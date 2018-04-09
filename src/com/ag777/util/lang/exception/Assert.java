package com.ag777.util.lang.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * 有关 非空判断抛出异常的 工具类。
 * 
 * @author ag777
 * @version create on 2018年04月09日,last modify at 2018年04月09日
 */
public class Assert {

	private Assert() {}
	
	/**
	 * 判断obj是否为null,是的话抛出IllegalArgumentException异常
	 * @param obj
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void illegalArgument(Object obj, String errMsg) throws IllegalArgumentException {
		if(obj == null) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断obj是否为null,是的话抛出异常
	 * @param obj
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable>void throwable(Object obj, Class<T> clazz, String errMsg) throws T {
		if(obj == null) {
			try {
				throw clazz.getConstructor(String.class).newInstance(errMsg);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}

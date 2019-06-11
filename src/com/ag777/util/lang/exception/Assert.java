package com.ag777.util.lang.exception;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import com.ag777.util.file.FileUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.CollectionAndMapUtils;

/**
 * 有关 非空判断抛出异常的 工具类。
 * 
 * @author ag777
 * @version create on 2018年04月09日,last modify at 2018年04月10日
 */
public class Assert {

	private Assert() {}
	
	//--notNull
	/**
	 * 判断obj是否为null,是的话抛出IllegalArgumentException异常
	 * @param obj
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void notNull(Object obj, String errMsg) throws IllegalArgumentException {
		if(obj == null) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断obj是否为null,是的话抛出异常
	 * @param obj
	 * @param clazz 集成与Throwable
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable>void notNull(Object obj, Class<T> clazz, String errMsg) throws T {
		if(obj == null) {
			throwable(clazz, errMsg);
		}
	}
	
	//-notEmpty
	/**
	 * 判断字符串是否为null或者为空,是的话抛出异常
	 * @param str
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void notEmpty(String str, String errMsg) throws IllegalArgumentException {
		if(StringUtils.isEmpty(str)) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断字符串是否为null或者为空,是的话抛出异常
	 * @param str
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable>void notEmpty(String str, Class<T> clazz, String errMsg) throws T {
		if(StringUtils.isEmpty(str)) {
			throwable(clazz, errMsg);
		}
	}
	
	/**
	 * 判断Collection是否为null或者为空,是的话抛出异常
	 * @param collection
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void notEmpty(Collection<?> collection, String errMsg) throws IllegalArgumentException {
		if(CollectionAndMapUtils.isEmpty(collection)) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断Collection是否为null或者为空,是的话抛出异常
	 * @param collection
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable>void notEmpty(Collection<?> collection, Class<T> clazz, String errMsg) throws T {
		if(CollectionAndMapUtils.isEmpty(collection)) {
			throwable(clazz, errMsg);
		}
	}
	
	/**
	 * 判断数组是否为null或者为空,是的话抛出异常
	 * @param array
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static <T>void notEmpty(T[] array, String errMsg) throws IllegalArgumentException {
		if(CollectionAndMapUtils.isEmpty(array)) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断数组是否为null或者为空,是的话抛出异常
	 * @param array
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable, V>void notEmpty(V[] array, Class<T> clazz, String errMsg) throws T {
		if(CollectionAndMapUtils.isEmpty(array)) {
			throwable(clazz, errMsg);
		}
	}
	
	/**
	 * 判断map是否为null或者为空,是的话抛出异常
	 * @param map
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static <K, V>void notEmpty(Map<K,V> map, String errMsg) throws IllegalArgumentException {
		if(CollectionAndMapUtils.isEmpty(map)) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断map是否为null或者为空,是的话抛出异常
	 * @param map
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable, K, V>void notEmpty(Map<K,V> map, Class<T> clazz, String errMsg) throws T {
		if(CollectionAndMapUtils.isEmpty(map)) {
			throwable(clazz, errMsg);
		}
	}
	
	//--notBlank
	/**
	 * 判断字符串是否为null或者只含空格,是的话抛出异常
	 * @param str
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void notBlank(String str, String errMsg) throws IllegalArgumentException {
		if(StringUtils.isBlank(str)) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断字符串是否为null或者只含空格,是的话抛出异常
	 * @param str
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable>void notBlank(String str, Class<T> clazz, String errMsg) throws T {
		if(StringUtils.isBlank(str)) {
			throwable(clazz, errMsg);
		}
	}
	
	//--notExisted
	/**
	 * 判断文件是否文件是否存在,不存在的话抛出异常
	 * @param filePath
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void notExisted(String filePath, String errMsg) throws IllegalArgumentException {
		if(!FileUtils.fileExists(filePath)) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 * 判断文件是否文件是否存在,不存在的话抛出异常
	 * @param filePath
	 * @param clazz 
	 * @param errMsg
	 * @throws T
	 */
	public static <T extends Throwable>void notExisted(String filePath, Class<T> clazz, String errMsg) throws T {
		if(!FileUtils.fileExists(filePath)) {
			throwable(clazz, errMsg);
		}
	}
	
	/**
	 * 判断文件是否文件是否存在,不存在的话抛出异常
	 * @param file
	 * @param errMsg
	 * @throws IllegalArgumentException
	 */
	public static void notExisted(File file, String errMsg) throws IllegalArgumentException {
		if(file == null || !file.exists()) {
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	/**
	 *  判断class是否存在,不存在的话抛出异常
	 * @param file 文件
	 * @param clazz 指定的异常抛出类
	 * @param errMsg 错误信息
	 * @throws T 执行的抛出异常
	 */
	public static <T extends Throwable>void notExisted(File file, Class<T> clazz, String errMsg) throws T {
		if(file == null || !file.exists()) {
			throwable(clazz, errMsg);
		}
	}
	
	/*=======================内部方法======================*/
	/**
	 * 实例化异常并抛出
	 * @param clazz
	 * @param errMsg
	 * @throws T
	 */
	private static <T extends Throwable>void throwable(Class<T> clazz, String errMsg) throws T {
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
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}

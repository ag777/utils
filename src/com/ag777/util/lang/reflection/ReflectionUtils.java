package com.ag777.util.lang.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类。
 * 
 * @author ag777
 * @version create on 2017年09月30日,last modify at 2017年11月03日
 */
public class ReflectionUtils {
	
	private ReflectionUtils() {}
	
	/**
	 * 判断一个类是否是数值类型,不能直接用Number.class.isAssignableFrom(clazz)判断
	 * @param clazz
	 * @return
	 */
	public static boolean isNumberType(Class<?> clazz) {
	    // 判断包装类
	    if (Number.class.isAssignableFrom(clazz)) {
	        return true;
	    }
	    // 判断原始类,过滤掉特殊的基本类型
	    if (clazz == boolean.class || clazz == char.class || clazz == void.class) {
	        return false;
	    }
	    return clazz.isPrimitive();
	}
	
	/**
	 * 判断一个类是否为基础类型
	 * @param clazz
	 * @return
	 */
	public static boolean isBasicClass(Class<?> clazz) {
		return clazz.isAssignableFrom(Integer.class) ||	//clazz是基本类型
				clazz.isAssignableFrom(Byte.class) ||
				clazz.isAssignableFrom(Short.class) ||
				clazz.isAssignableFrom(Long.class) ||
				clazz.isAssignableFrom(Float.class) ||
				clazz.isAssignableFrom(Double.class) ||
				clazz.isAssignableFrom(Boolean.class) ||
				clazz.isAssignableFrom(Character.class) ||
				clazz.isAssignableFrom(String.class);
	}
	
	/**
	 * 通过注释获取变量列表
	 * @param annotationClass
	 * @return
	 */
	public static List<Field> getFieldListByAnnotation(Class<?> clazz,Class<? extends Annotation> annotationClass) {
		List<Field> result = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(annotationClass)) {
				result.add(field);
			}
		}
		return result;
	}
	
	/**
	 * 通过注释获取方法列表
	 * @param annotationClass
	 * @return
	 */
	public static List<Method> getMethodListByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		List<Method> result = new ArrayList<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if(method.isAnnotationPresent(annotationClass)) {
				result.add(method);
			}
		}
		return result;
	}
	
	/**
	 * 获取成员变量对应的值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		try {
			return obj.getClass().getDeclaredField(fieldName).get(obj);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断类是否在包底下
	 * <p>
	 * 	通过String.startsWith()函数判断
	 * </p>
	 * @param clazz
	 * @param packageName
	 * @return
	 */
	public static boolean inPackage(Class<?> clazz, String packageName) {
		if(clazz == null || packageName == null) {
			return false;
		}
		if(clazz.getName().startsWith(packageName+".")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 实例化class对象,支持内部类
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public static <T>T newInstace(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		Class<?> outerClass = getOuterClass(clazz);
		if(outerClass == null) {	//不是内部类
			return clazz.newInstance();
		}
		
		T obj = null;
		
		Constructor<?>[] c = clazz.getDeclaredConstructors();
		int modifier_class = clazz.getModifiers();
		boolean flag = c[0].isAccessible();	//记录原本的可访问性
		c[0].setAccessible(true);
		
		if(Modifier.isStatic(modifier_class)) {	//带static的类直接实例化对象
			obj =  (T) c[0].newInstance();
		}  else {	//其余的都需要先实例化外部对象再实例化内部类
			obj = (T) c[0].newInstance(newInstace(outerClass));
		}
		
		c[0].setAccessible(flag);	//还原可访问性
		return obj;
	}
	
	/**
	 * 获取内部类的外包类
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static <T>Class<?> getOuterClass(Class<T> clazz) throws ClassNotFoundException {
		String className = clazz.getName();
		if(className.contains("$")) {
			return Class.forName(className.split("\\$")[0]);
		}
		return null;
	}
}

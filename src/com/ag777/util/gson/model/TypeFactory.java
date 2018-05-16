package com.ag777.util.gson.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 便捷地获取带泛型类的Type
 * <p>
 * 比如:
 * <pre>
 * new TypeFactory(List.class, String.class) => List<String>
 * new TypeFactory(Map.class, String.class, Object.class) => Map<String, Object>
 * </pre>
 * </p>
 * 
 * @author ag777
 * @version create on 2018年05月16日,last modify at 2018年05月16日
 */
public class TypeFactory implements ParameterizedType {

	private Class<?> rawClass;
	private Class<?> argumentsClass[];

	public TypeFactory(Class<?> rawClass, Class<?>... argumentsClass) {
		this.rawClass = rawClass;
		this.argumentsClass = argumentsClass;
	}

	@Override
	public Type[] getActualTypeArguments() {
		return argumentsClass;
	}

	@Override
	public Type getOwnerType() {
		return null;
	}

	@Override
	public Type getRawType() {
		return rawClass;
	}
}

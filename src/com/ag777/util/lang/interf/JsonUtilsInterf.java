package com.ag777.util.lang.interf;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface JsonUtilsInterf {

	public String toJson(Object obj);
	
	public Map<String, Object> toMap(String json) throws Exception;
	
	public List<Map<String, Object>> toListMap(String json) throws Exception;
	
	public <T>T fromJson(String json, Class<T> classOfT) throws Exception;
	
	public <T>T fromJson(String json, Type type);
}

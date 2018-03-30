package com.ag777.util.lang.interf;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * json转化接口
 * 
 * @author wanggz
 * @version last modify at 2018年03月30日
 */
public interface JsonUtilsInterf {

	public String toJson(Object obj);
	
	public Map<String, Object> toMap(String json);
	
	public Map<String, Object> toMapWithException(String json) throws Exception;
	
	public List<Map<String, Object>> toListMap(String json);
	
	public List<Map<String, Object>> toListMapWithException(String json) throws Exception;
	
	public <T>T fromJson(String json, Class<T> classOfT);
	
	public <T>T fromJsonWithException(String json, Class<T> classOfT) throws Exception;
	
	public <T>T fromJson(String json, Type type);
	
	public <T>T fromJsonWithException(String json, Type type) throws Exception;
}

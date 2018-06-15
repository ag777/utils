package com.ag777.util.lang.interf;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.ag777.util.lang.exception.model.JsonSyntaxException;

/**
 * json转化接口
 * 
 * @author wanggz
 * @version last modify at 2018年06月15日
 */
public interface JsonUtilsInterf {

	/**
	 * 格式化json串
	 * @param src
	 * @return
	 */
	public String prettyFormat(String str)  throws JsonSyntaxException;
	
	/**
	 * 对象转json
	 * @param obj
	 * @return
	 */
	public String toJson(Object obj);
	
	/**
	 * json转Map<String, Object>
	 * @param json
	 * @return
	 */
	public Map<String, Object> toMap(String json);
	
	public Map<String, Object> toMapWithException(String json) throws JsonSyntaxException;
	
	/**
	 * json转List<T>
	 * @param json
	 * @return
	 */
	public <T>List<T> toList(String json, Class<T> classOfT);
	
	public <T>List<T> toListWithException(String json, Class<T> classOfT) throws JsonSyntaxException;
	
	/**
	 * json转List<Map<String, Object>>
	 * @param json
	 * @return
	 */
	public List<Map<String, Object>> toListMap(String json);
	
	public List<Map<String, Object>> toListMapWithException(String json) throws JsonSyntaxException;
	
	/**
	 * json转任意对象
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public <T>T fromJson(String json, Class<T> classOfT);
	
	public <T>T fromJsonWithException(String json, Class<T> classOfT) throws JsonSyntaxException;
	
	/**
	 * json转任意对象
	 * @param json
	 * @param type
	 * @return
	 */
	public <T>T fromJson(String json, Type type);
	
	public <T>T fromJsonWithException(String json, Type type) throws JsonSyntaxException;
}

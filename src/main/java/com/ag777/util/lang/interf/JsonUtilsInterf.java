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
	 * @param str str
	 * @return
	 */
	public String prettyFormat(String str)  throws JsonSyntaxException;
	
	/**
	 * 对象转json
	 * @param obj obj
	 * @return
	 */
	public String toJson(Object obj);
	
	/**
	 * json转<pre>{@code Map<String, Object> }</pre>
	 * @param json json
	 * @return
	 */
	public Map<String, Object> toMap(String json);
	
	public Map<String, Object> toMapWithException(String json) throws JsonSyntaxException;
	
	/**
	 * json转<pre>{@code List<T> }</pre>
	 * @param json json
	 * @return
	 */
	public <T>List<T> toList(String json, Class<T> classOfT);
	
	public <T>List<T> toListWithException(String json, Class<T> classOfT) throws JsonSyntaxException;
	
	/**
	 * json转<pre>{@code List<Map<String, Object>> }</pre>
	 * @param json json
	 * @return
	 */
	public List<Map<String, Object>> toListMap(String json);
	
	public List<Map<String, Object>> toListMapWithException(String json) throws JsonSyntaxException;
	
	/**
	 * json转任意对象
	 * @param json json
	 * @param classOfT classOfT
	 * @return
	 */
	public <T>T fromJson(String json, Class<T> classOfT);
	
	public <T>T fromJsonWithException(String json, Class<T> classOfT) throws JsonSyntaxException;
	
	/**
	 * json转任意对象
	 * @param json json
	 * @param type type
	 * @return
	 */
	public <T>T fromJson(String json, Type type);
	
	public <T>T fromJsonWithException(String json, Type type) throws JsonSyntaxException;
}
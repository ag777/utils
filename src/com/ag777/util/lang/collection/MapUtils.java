package com.ag777.util.lang.collection;

import java.util.List;
import java.util.Map;
import com.ag777.util.lang.ObjectUtils;

/**
 * 有关 <code>Map</code> 哈希表工具类。
 * 
 * @author ag777
 * @version create on 2017年09月22日,last modify at 2017年09月22日
 */
public class MapUtils {

	
	public static <K,V>Map<K,V> newHashMap() {
		return CollectionAndMapUtils.newHashMap();
	}
	
	public static <K,V>Map<K,V> newHashTable() {
		return CollectionAndMapUtils.newHashTable();
	}
	
	
	/**
     * 将List<Map> 中的键值从新组成map(不做类型转化判断，就是说结果类型要是对不上，强转报错请注意)
     * <p>
     * 可以理解为纵表转横表
     * 例如：
     * <p/>
     * 
     * <pre>
     * 		现有值为[{a:key1,b:2}{a:key2,b:3}]的list
     *		MapUtils.toMap(list, "a", "b") = {key1:2, key2:3}
     * </pre>
     * <p/>
     * </p>
     * 
     * @param list 校验的类
     * @param keyTilte 作为新map的key的键
     * @param keyValue 作为新map的值的键
     * @return 
     */
	public static Map<String, Object> toMap(List<Map<String, Object>> list, String keyTilte, String keyValue) {
		if(list == null) {
			return CollectionAndMapUtils.newHashMap();
		}
		Map<String, Object> map = CollectionAndMapUtils.newHashMap();
		for (Map<String, Object> item : list) {
			if(item.containsKey(keyTilte) && item.get(keyTilte) != null && item.containsKey(keyValue)) {
				try {
					map.put(item.get(keyTilte).toString(), item.get(keyValue));
				} catch(Exception ex) {
				}
			}
		}
		return map;
	}
	
	/**
     * 获取map里key对应的值，不存在或null返回defaultValue
     * <p>
     * 例如：
     * <p/>
     * 
     * <pre>
     * 		现有值为{a:1,b:2}的map
     *		MapUtils.get(map, a, 2) = 1
     *		MapUtils.get(map, c, 2) = 2
     * </pre>
     * <p/>
     * </p>
     * 
     * @param map 校验的类
     * @param key 键
     * @param defaultValue 默认值
     * @return 
     */
	@SuppressWarnings("unchecked")
	public static <K,V,T>T get(Map<K, V> map, K key, T defaultValue) {
		try {
			if(map.containsKey(key) && map.get(key) != null) {
				return (T) map.get(key);
			}
		}catch(Exception ex) {
		}
		return defaultValue;
	}
	
	
	/**
     * 获取map里key对应的值，不存在或null返回null
     * <p>
     * 例如：
     * <p/>
     * 
     * <pre>
     * 		现有值为{a:1,b:2}的map
     *		MapUtils.get(map, a) = 1
     *		MapUtils.get(map, c) = null
     * </pre>
     * <p/>
     * </p>
     * 
     * @param map 校验的类
     * @param key 键
     * @return 
     */
	public static <K,V,T>T get(Map<K, V> map, K key) {
		return get(map, key, null);
	}
	
	/**
	 * 获取String
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V>String getString(Map<K, V> map, K key) {
		return ObjectUtils.toString(
				get(map, key));
	}
	
	/**
	 * 获取String
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K,V>String getString(Map<K, V> map, K key, String defaultValue) {
		return ObjectUtils.toString(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Double
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V>Double getDouble(Map<K, V> map, K key) {
		return ObjectUtils.toDouble(
				get(map, key));
	}
	
	/**
	 * 获取double
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K,V>double getDouble(Map<K, V> map, K key, double defaultValue) {
		return ObjectUtils.toDouble(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Float
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V>Float getFloat(Map<K, V> map, K key) {
		return ObjectUtils.toFloat(
				get(map, key));
	}
	
	/**
	 * 获取float
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K,V>float getFloat(Map<K, V> map, K key, float defaultValue) {
		return ObjectUtils.toFloat(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Integer
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V>Integer getInteger(Map<K, V> map, K key) {
		return ObjectUtils.toInteger(
				get(map, key));
	}
	
	/**
	 * 获取int
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K,V>int getInteger(Map<K, V> map, K key, int defaultValue) {
		return ObjectUtils.toInteger(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Long
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V>Long getLong(Map<K, V> map, K key) {
		return ObjectUtils.toLong(
				get(map, key));
	}
	
	/**
	 * 或区域long
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K,V>long getLong(Map<K, V> map, K key, long defaultValue) {
		return ObjectUtils.toLong(
				get(map, key), defaultValue);
	}
	
}

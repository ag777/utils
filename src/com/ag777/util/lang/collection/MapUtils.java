package com.ag777.util.lang.collection;

import java.util.List;
import java.util.Map;

import com.ag777.util.lang.ObjectUtils;
import com.ag777.util.lang.StringUtils;

/**
 * @author ag777
 * @Description 哈希表工具类
 * Time: created at 2017/09/22. last modify at 2017/09/22.
 * Mark: 
 */
public class MapUtils {

	
	public static <K,V>Map<K,V> newHashMap() {
		return CollectionAndMapUtils.newHashMap();
	}
	
	public static <K,V>Map<K,V> newHashTable() {
		return CollectionAndMapUtils.newHashTable();
	}
	
	/**
	 * 获取map里key对应的值，不存在或null返回defaultValue
	 * @param map
	 * @param key
	 * @param defaultValue
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
	 * @param map
	 * @param key
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
	
	/**
	 * 将List<Map> 中的键值从新组成map(不做类型转化判断，就是说结果类型要是对不上，强转报错请注意)
	 * @param list 源列表 List<Map>,为null返回空列表
	 * @param key_tilte 源List<Map>中key_tilte对应的值(不允许为null)作为结果Map中的key
	 * @param key_value 源List<Map>中key_value对应的值作为结果Map中的value
	 * @return
	 */
	public static Map<String, Object> toMap(List<Map<String, Object>> list, String key_tilte, String key_value) {
		if(list == null) {
			return CollectionAndMapUtils.newHashMap();
		}
		Map<String, Object> map = CollectionAndMapUtils.newHashMap();
		for (Map<String, Object> item : list) {
			if(item.containsKey(key_tilte) && item.get(key_tilte) != null && item.containsKey(key_value)) {
				try {
					map.put(item.get(key_tilte).toString(), item.get(key_value));
				} catch(Exception ex) {
				}
			}
		}
		return map;
	}
	
}

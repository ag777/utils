package com.ag777.util.lang.collection;

import com.ag777.util.lang.ObjectUtils;
import com.ag777.util.lang.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 有关 <code>Map</code> 哈希表工具类。
 * 
 * @author ag777
 * @version create on 2017年09月22日,last modify at 2022年10月25日
 */
public class MapUtils {

	public static <K,V>Map<K,V> newMap() {
		return newHashMap();
	}
	
	public static <K,V>HashMap<K,V> newHashMap() {
		return CollectionAndMapUtils.newHashMap();
	}
	
	public static <K,V>LinkedHashMap<K,V> newLinkedHashMap() {
		return CollectionAndMapUtils.newLinkedHashMap();
	}
	
	@Deprecated
	public static <K,V>Hashtable<K,V> newHashTable() {
		return CollectionAndMapUtils.newHashtable();
	}
	
	/**
	 * @see com.ag777.util.lang.collection.CollectionAndMapUtils#newConcurrentHashMap()
	 * @return
	 */
	public static <K, V>ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return CollectionAndMapUtils.newConcurrentHashMap();
	}
	
	public static <K, V>boolean isEmpty(Map<K, V> map) {
		return CollectionAndMapUtils.isEmpty(map);
	}
	
	/**
	 * 相当于新建一个map并执行putAll，用于深度拷贝map
	 * @param map map
	 * @return
	 */
	public static <K, V>Map<K, V> of(Map<K, V> map) {
		HashMap<K, V> result = newHashMap();
		putAll(result, map);
		return result;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含一对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key, Object value) {
		return of(String.class, Object.class, key, value);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key, V value) {
		Map<K, V> map = newMap();
		map.put(key, value);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含两对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2) {
		return of(String.class, Object.class, key1, value1, key2, value2);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1);
		map.put(key2, value2);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含三对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2);
		map.put(key3, value3);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含四对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3);
		map.put(key4, value4);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含五对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3, key4, value4);
		map.put(key5, value5);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含六对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5, String key6, Object value6) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5);
		map.put(key6, value6);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含七对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5, String key6, Object value6, String key7, Object value7) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6);
		map.put(key7, value7);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含八对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5, String key6, Object value6, String key7, Object value7, String key8, Object value8) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7);
		map.put(key8, value8);
		return map;
	}
	
	/**
	 * 构建map
	 * <p>
	 * 	含九对key-value
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5, String key6, Object value6, String key7, Object value7, String key8, Object value8, String key9, Object value9) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9);
	}
	
	/**
	 * 构建map
	 */
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8);
		map.put(key9, value9);
		return map;
	}
	
	/**
	 * 构建map,采用了强转的方式实现,请自行保证参数类型正确性
	 * <p>
	 * 	含十对key-value及以上
	 * </p>
	 * 
	 */
	public static Map<String, Object> of(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5, String key6, Object value6, String key7, Object value7, String key8, Object value8, String key9, Object value9, String key10, Object value10, Object... others) {
		return of(String.class, Object.class, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9, key10, value10, others);
	}
	
	/**
	 * 构建map,采用了强转的方式实现,请自行保证参数类型正确性(others长度为偶数)
	 */
	@SuppressWarnings("unchecked")
	public static <K, V>Map<K, V> of(Class<K> clazzT, Class<V> clazzV, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10, Object... others) {
		Map<K, V> map = of(clazzT, clazzV, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5, key6, value6, key7, value7, key8, value8, key9, value9);
		map.put(key10, value10);
		if(others != null) {
			for (int i = 0; i < others.length; i=i+2) {
				map.put((K)others[i], (V)others[i+1]);
			}
		}
		return map;
	}

	/**
	 *
	 * @param key 第一项的键
	 * @param value 第一项的值
	 * @param others 其它项,参数数量必须是2的倍数
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> ofLinked(String key, Object value, Object... others) {
		return ofLinked(String.class, Object.class, key, value, others);
	}

	/**
	 * 构建LinkedHashMap,采用了强转的方式实现,请自行保证参数类型正确性(others长度为偶数)
	 * @param clazzT 键的类型
	 * @param clazzV 值的类型
	 * @param key1 第一项的键
	 * @param value1 第一项的值
	 * @param others 其它项,参数数量必须是2的倍数
	 * @param <K> K
	 * @param <V> V
	 * @return Map<K, V>
	 */
	public static <K, V>Map<K, V> ofLinked(Class<K> clazzT, Class<V> clazzV, K key1, V value1, Object... others) {
		int size = 1;
		if(others != null)  {
			size += others.length / 2;
		}
		Map<K, V> map = new LinkedHashMap<>(size);
		putAll(map, key1, value1);
		return putAll(map, others);
	}
	
	/**
	 * 如果条件成立则往map里插值
	 * <p>该方法不进行map的空指针判断
	 * @param map map
	 * @param key 键
	 * @param value 值
	 * @param predicate 判断类
	 * @return 条件是否成立
	 */
	public static <K, V>boolean putIf(Map<K, V> map, K key, V value, Predicate<V> predicate) {
		if(predicate.test(value)) {
			map.put(key, value);
			return true;
		}
		return false;
	}
	
	/**
	 * 往map里插入任意多的键值对,采用了强转的方式实现,请自行保证参数类型正确性(others长度为偶数)
	 * @param map map
	 * @param predicate 返回true则插入
	 * @param others others
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V>Map<K, V> putAllIf(Map<K, V> map, BiPredicate<K, V> predicate, Object... others) {
		if(map == null) {
			map = MapUtils.newHashMap();
		}
		if(others != null) {
			for (int i = 0; i < others.length; i=i+2) {
				K key = (K)others[i];
				V val = (V) others[i+1];
				if(predicate !=null && predicate.test(key, val))
				map.put(key, val);
			}
		}
		return map;
	}
	
	/**
	 * 往map里插入任意多的键值对,采用了强转的方式实现,请自行保证参数类型正确性(others长度为偶数)
	 * @param map 原始map
	 * @param others 需要插入的键值对
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V>Map<K, V> putAll(Map<K, V> map, Object... others) {
		if(map == null) {
			map = MapUtils.newHashMap();
		}
		if(others != null) {
			for (int i = 0; i < others.length; i++) {
				map.put((K)others[i], (V)others[++i]);
			}
		}
		return map;
	}
	
	/**
     * 将<pre>{@code List<Map> }</pre> 中的键值从新组成map(不做类型转化判断，就是说结果类型要是对不上，强转报错请注意)
     * <p>
     * 可以理解为纵表转横表
     * 例如：
     * 
     * <pre>{@code
     * 		现有值为[{a:key1,b:2}{a:key2,b:3}]的list
     *		MapUtils.of(list, "a", "b") = {key1:2, key2:3}
     * }</pre>
     * 
     * @param list 校验的类
     * @param keyTilte 作为新map的key的键
     * @param keyValue 作为新map的值的键
     * @return 
     */
	public static Map<String, Object> ofMap(List<Map<String, Object>> list, String keyTilte, String keyValue) {
		if(list == null) {
			return newMap();
		}
		Map<String, Object> map = newMap();
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
	 * 根据分隔符拆分字符串得到map
	 * <p>
	 * 	注意分割服都是正则(注意转义问题)<br>
	 * key-value分割符无法拆分的项会被略过,不会报错
	 * 
	 * @param src src
	 * @param separatorItem 分割map每一项的分隔符
	 * @param separatorKeyValue	分割每一项key-value的分隔符
	 * @return linkedHashMap 保留先后顺序
	 */
	public static Map<String, Object> ofMap(String src, String separatorItem, String separatorKeyValue) {
		if(StringUtils.isBlank(src)) {
			return newLinkedHashMap();
		}
		Map<String, Object> result = newLinkedHashMap();
		String[] groups = src.split(separatorItem);
		for (String item : groups) {
			String[] itemGroup = item.split(separatorKeyValue);
			if(itemGroup.length > 1) {
				result.put(itemGroup[0], itemGroup[1]);
			}
		}
		return result;
	}
	
	/**
	 * 空指针安全put
	 * @param map map
	 * @param key key
	 * @param value value
	 * @return
	 */
	public static <K,V>Map<K, V> put(Map<K, V> map, K key, V value) {
		if(map == null) {
			map = newHashMap();
		}
		map.put(key, value);
		return map;
	}
	
	/**
	 * 空指针安全putAll
	 * @param map map
	 * @param extendMap extendMap
	 * @return
	 */
	public static <K,V>Map<K, V> putAll(Map<K, V> map, Map<K, V> extendMap) {
		if(map == null) {
			map = newHashMap();
		}
		if(extendMap != null) {
			map.putAll(extendMap);
		}
		return map;
	}
	
	/**
     * 获取map里key对应的值，不存在或null返回defaultValue
     * <p>
     * 例如：
     * </p><pre>{@code
     * 		现有值为{a:1,b:2}的map
     *		MapUtils.get(map, a, 2) = 1
     *		MapUtils.get(map, c, 2) = 2
     * }</pre>
     * 
     * @param map 校验的类
     * @param key 键
     * @param defaultValue 默认值
     * @return 
     */
	@SuppressWarnings("unchecked")
	public static <K,V,T>T get(Map<K, V> map, K key, T defaultValue) {
		try {
			if(map != null && map.containsKey(key) && map.get(key) != null) {
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
     * </p><pre>{@code
     * 		现有值为{a:1,b:2}的map
     *		MapUtils.get(map, a) = 1
     *		MapUtils.get(map, c) = null
     * }</pre>
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
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>String getStr(Map<K, V> map, K key) {
		return ObjectUtils.toStr(
				get(map, key));
	}
	
	/**
	 * 获取String
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>String getStr(Map<K, V> map, K key, String defaultValue) {
		return ObjectUtils.toStr(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Double
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>Double getDouble(Map<K, V> map, K key) {
		return ObjectUtils.toDouble(
				get(map, key));
	}
	
	/**
	 * 获取double
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>double getDouble(Map<K, V> map, K key, double defaultValue) {
		return ObjectUtils.toDouble(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Float
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>Float getFloat(Map<K, V> map, K key) {
		return ObjectUtils.toFloat(
				get(map, key));
	}
	
	/**
	 * 获取float
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>float getFloat(Map<K, V> map, K key, float defaultValue) {
		return ObjectUtils.toFloat(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Integer
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>Integer getInt(Map<K, V> map, K key) {
		return ObjectUtils.toInt(
				get(map, key));
	}
	
	/**
	 * 获取int
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>int getInt(Map<K, V> map, K key, int defaultValue) {
		return ObjectUtils.toInt(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Long
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>Long getLong(Map<K, V> map, K key) {
		return ObjectUtils.toLong(
				get(map, key));
	}
	
	/**
	 * 获取Long
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>long getLong(Map<K, V> map, K key, long defaultValue) {
		return ObjectUtils.toLong(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Boolean
	 * <p>
	 * 	当字符串为"true"或者"1"时返回true
	 * 	当字符串为"false"或者"0"时返回flase
	 * 	其余情况返回null
	 * </p>
	 * 
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>Boolean getBoolean(Map<K, V> map, K key) {
		return ObjectUtils.toBoolean(
				get(map, key));
	}
	
	/**
	 * 获取Boolean
	 * <p>
	 * 	当字符串为"true"或者"1"时返回true
	 * 	当字符串为"false"或者"0"时返回flase
	 * 	其余情况返回null
	 * </p>
	 * 
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>boolean getBoolean(Map<K, V> map, K key, boolean defaultValue) {
		return ObjectUtils.toBoolean(
				get(map, key), defaultValue);
	}
	
	/**
	 * 获取Date
	 * <p>
	 * 	支持四种格式
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd HH:mm
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 * 
	 * @param map map
	 * @param key key
	 * @return
	 */
	public static <K,V>Date getDate(Map<K, V> map, K key) {
		return ObjectUtils.toDate(
				get(map, key));
	}
	
	/**
	 * 获取Date
	 * <p>
	 * 支持三种格式
	 *	 yyyy-MM-dd HH:mm:ss
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 * 
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <K,V>Date getDate(Map<K, V> map, K key, Date defaultValue) {
		return ObjectUtils.toDate(
				get(map, key), defaultValue);
	}
	
	public <K,V> Map<K,V>remove(Map<K, V> map, K key) {
		if(map == null) {
			return null;
		}
		map.remove(key);
		return map;
	}
	
	/**
	 * 转换map中的key
	 * 
	 * @param src src
	 * @param convertMap 源key和转换的目标key的对应map
	 * @return
	 */
	public static <K,V>Map<K,V> convertKeys(Map<K,V> src, Map<K,K> convertMap) {
		Iterator<K> itor = convertMap.keySet().iterator();
		while(itor.hasNext()) {
			K key1 = itor.next();
			K key2 = convertMap.get(key1);
			convertKey(src, key1, key2);
		}
		return src;
	}
	
	/**
	 * 转换map中的key
	 * <p>
	 * 	流程:判断源map中是否有键key1,保存key2及key1对应的值，删除键key1，返回源map
	 * </p>
	 * 
	 * @param src src
	 * @param key1 key1
	 * @param key2 key2
	 * @return
	 */
	public static <K,V>Map<K,V> convertKey(Map<K,V> src, K key1, K key2) {
		if(isEmpty(src)) {
			return src;
		}
		if(src.containsKey(key1)) {
			src.put(key2, src.get(key1));
			src.remove(key2);
		}
		return src;
	}
	
	/**
	 * 格式化map为字符串
	 * @param map map
	 * @param separatorItem separatorItem
	 * @param separatorKeyValue separatorKeyValue
	 * @return
	 */
	public static <K,V>String toString(Map<K,V> map, String separatorItem, String separatorKeyValue) {
		if(isEmpty(map)) {
			return "";
		}
		StringBuilder sb = null;
		Iterator<K> itor = map.keySet().iterator();
		while(itor.hasNext()) {
			if(sb == null) {
				sb = new StringBuilder();
			} else if(separatorItem != null){
				sb.append(separatorItem);
			}
			K key = itor.next();
			V value = map.get(key);
			sb.append(key);
			if(separatorKeyValue != null) {
				sb.append(separatorKeyValue);
			}
			sb.append(value);
		}
		return sb.toString();	//理论上不用考虑sb为null的情况，因为map不为空
	}
	
	/**
	 * 对linkedHashedMap进行排序
	 * <p>
	 * 参考:https://blog.csdn.net/qq997404392/article/details/73333215
	 * </p>
	 * @param src src
	 * @param comparator comparator
	 * @return
	 */
	public static <K,V>Map<K,V> sort(LinkedHashMap<K,V> src, Comparator<Map.Entry<K, V>> comparator) {
		if(isEmpty(src)) {
			return src;
		}
		//先转成ArrayList集合
		List<Entry<K, V>> list = 
		        new ArrayList<>(src.entrySet());
		Collections.sort(list, comparator);
		//清空源map，把排序后的List放入
		src.clear();
		for (Map.Entry<K, V> entry : list) {
			src.put(entry.getKey(), entry.getValue());
		}
		return src;
	}
	
}
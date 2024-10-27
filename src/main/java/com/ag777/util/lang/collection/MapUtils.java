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
 * @version create on 2017年09月22日,last modify at 2024年10月27日
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
		}catch(Exception ignored) {
		}
		return defaultValue;
	}

	/**
	 * 从Map中获取第一个匹配的键对应的值
	 * 如果Map为空、没有匹配的键、或匹配的值为null，则返回默认值
	 * 此方法用于在多个备选键中寻找第一个有效（非null）的值
	 *
	 * @param <K> 键的类型
	 * @param <V> 值的类型
	 * @param <T> 返回值的类型，与V兼容
	 * @param map 要查询的Map
	 * @param keys 备选键数组，按此顺序查找
	 * @param defaultValue 如果找不到非null值时返回的默认值
	 * @return 第一个匹配的非null值，否则返回默认值
	 */
	public static <K,V,T>T getFirst(Map<K, V> map, K[] keys, T defaultValue) {
	    // 检查Map是否为空或为空Map，如果是，则直接返回null
	    if (map == null || map.isEmpty()) {
	        return null;
	    }
	    try {
	        // 遍历备选键数组
	        for (K key : keys) {
	            // 检查当前键是否在Map中存在
	            if (map.containsKey(key)) {
	                V v = map.get(key);
	                // 如果找到的值非null，则将其转换为T类型并返回
	                if (v != null) {
	                    return (T) v;
	                }
	            }
	        }
	    } catch (Exception ignored) {
	        // 忽略任何异常，即使出现异常也不影响程序继续执行
	    }
	    // 如果所有备选键都不匹配或匹配的值均为null，则返回默认值
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
	 * 从映射中获取第一个匹配的值
	 *
	 * @param <K> 键的类型
	 * @param <V> 值的类型
	 * @param <T> 返回值的类型
	 * @param map 要查询的映射
	 * @param keys 用于查询的键数组
	 * @return 第一个匹配的值，如果找不到则返回null
	 */
	public static <K,V,T>T getFirst(Map<K, V> map, K[] keys) {
	    return getFirst(map, keys, null);
	}

	/**
	 * 从给定的Map中获取与指定key关联的值，并将其转换为字符串表示形式
	 * 如果指定的key在Map中不存在，或者其关联的值为null，则返回空字符串
	 * 此方法结合了Map的get操作和对象到字符串的转换操作，简化了处理流程
	 *
	 * @param map 要从中获取值的Map
	 * @param key 要获取其关联值的键
	 * @return 与指定key关联的值的字符串表示形式，如果值为null或key不存在，则返回空字符串
	 */
	public static <K,V> String getStr(Map<K, V> map, K key) {
	    return ObjectUtils.toStr(
	            get(map, key));
	}
	
	/**
	 * 获取String
	 * @param map map
	 * @param key key
	 * @param defaultValue defaultValue
	 * @return 与指定key关联的值的字符串表示形式，如果值为null或key不存在，则返回空字符串
	 */
	public static <K,V>String getStr(Map<K, V> map, K key, String defaultValue) {
		return ObjectUtils.toStr(
				get(map, key), defaultValue);
	}

	/**
	 * 获取第一个匹配的键对应的值并转换为字符串
	 * 如果没有找到匹配的键或者值为null，则返回空字符串
	 * 此方法用于简化从Map中获取值并进行类型转换的过程
	 *
	 * @param map   要查询的Map
	 * @param keys  可能的键数组，方法会尝试使用每个键来获取值，直到找到第一个匹配的键
	 * @param <K>   键的类型
	 * @param <V>   值的类型
	 * @return      第一个匹配的键对应的值的字符串表示，如果没有找到则返回空字符串
	 */
	public static <K,V> String getFirstStr(Map<K, V> map, K[] keys) {
	    return ObjectUtils.toStr(
	            getFirst(map, keys));
	}

	/**
	 * 获取第一个匹配的键对应的值并转换为字符串
	 * 如果没有找到匹配的键或者值为null，则返回默认值
	 * 此方法扩展了getFirstStr，增加了默认值参数，使得在未找到匹配键时可以返回一个自定义的默认值
	 *
	 * @param map          要查询的Map
	 * @param keys         可能的键数组，方法会尝试使用每个键来获取值，直到找到第一个匹配的键
	 * @param defaultValue 默认值，如果没有找到匹配的键或者值为null时返回
	 * @param <K>          键的类型
	 * @param <V>          值的类型
	 * @return             第一个匹配的键对应的值的字符串表示，如果没有找到则返回默认值
	 */
	public static <K,V> String getFirstStr(Map<K, V> map, K[] keys, String defaultValue) {
	    return ObjectUtils.toStr(
	            getFirst(map, keys), defaultValue);
	}

	/**
	 * 从map中获取与指定key关联的值，并将其转换为Double类型。
	 * 如果指定的key不存在于map中，或者其关联的值无法转换为Double类型，则返回null。
	 *
	 * @param map 包含键值对的map。
	 * @param key 需要获取值的键。
	 * @return 转换为Double类型的值，如果无法获取或转换，则返回null。
	 */
	public static <K,V>Double getDouble(Map<K, V> map, K key) {
	    return ObjectUtils.toDouble(
	            get(map, key));
	}

	/**
	 * 从map中获取与指定key关联的值，并将其转换为double类型。
	 * 如果指定的key不存在于map中，或者其关联的值无法转换为double类型，则返回默认值。
	 *
	 * @param map 包含键值对的map。
	 * @param key 需要获取值的键。
	 * @param defaultValue 如果无法获取或转换值时返回的默认值。
	 * @return 转换为double类型的值，如果无法获取或转换，则返回默认值。
	 */
	public static <K,V>double getDouble(Map<K, V> map, K key, double defaultValue) {
	    return ObjectUtils.toDouble(
	            get(map, key), defaultValue);
	}

	/**
	 * 从map中获取与指定键数组中的第一个匹配键关联的值，并将其转换为Double类型。
	 * 如果键数组中的任何一个键在map中不存在，或者其关联的值无法转换为Double类型，则返回null。
	 *
	 * @param map 包含键值对的map。
	 * @param keys 包含可能的键的数组。
	 * @return 转换为Double类型的值，如果无法获取或转换，则返回null。
	 */
	public static <K,V>Double getFirstDouble(Map<K, V> map, K[] keys) {
	    return ObjectUtils.toDouble(
	            getFirst(map, keys));
	}

	/**
	 * 从map中获取与指定键数组中的第一个匹配键关联的值，并将其转换为Double类型。
	 * 如果键数组中的任何一个键在map中不存在，或者其关联的值无法转换为Double类型，则返回默认值。
	 *
	 * @param map 包含键值对的map。
	 * @param keys 包含可能的键的数组。
	 * @param defaultValue 如果无法获取或转换值时返回的默认值。
	 * @return 转换为Double类型的值，如果无法获取或转换，则返回默认值。
	 */
	public static <K,V>Double getFirstDouble(Map<K, V> map, K[] keys, double defaultValue) {
	    return ObjectUtils.toDouble(
	            getFirst(map, keys), defaultValue);
	}
	
	/**
	 * 从map中获取与指定key关联的值，并将其转换为Float类型。
	 * 如果key不存在或对应的值为null，则返回null。
	 *
	 * @param map 包含键值对的map。
	 * @param key 要获取值的键。
	 * @return 转换为Float类型的值，如果key不存在或对应的值为null，则返回null。
	 */
	public static <K,V>Float getFloat(Map<K, V> map, K key) {
	    return ObjectUtils.toFloat(
	            get(map, key));
	}

	/**
	 * 从map中获取与指定key关联的值，并将其转换为float类型。
	 * 如果key不存在或对应的值为null，则返回默认值defaultValue。
	 *
	 * @param map 包含键值对的map。
	 * @param key 要获取值的键。
	 * @param defaultValue 如果key不存在或对应的值为null时返回的默认值。
	 * @return 转换为float类型的值，如果key不存在或对应的值为null，则返回默认值。
	 */
	public static <K,V>float getFloat(Map<K, V> map, K key, float defaultValue) {
	    return ObjectUtils.toFloat(
	            get(map, key), defaultValue);
	}

	/**
	 * 从map中获取与指定keys数组中的第一个存在的key关联的值，并将其转换为Float类型。
	 * 如果所有key都不存在或对应的值为null，则返回null。
	 *
	 * @param map 包含键值对的map。
	 * @param keys 要获取值的键数组。
	 * @return 转换为Float类型的值，如果所有key都不存在或对应的值为null，则返回null。
	 */
	public static <K,V>Float getFirstFloat(Map<K, V> map, K[] keys) {
	    return ObjectUtils.toFloat(
	            getFirst(map, keys));
	}

	/**
	 * 从map中获取与指定keys数组中的第一个存在的key关联的值，并将其转换为float类型。
	 * 如果所有key都不存在或对应的值为null，则返回默认值defaultValue。
	 *
	 * @param map 包含键值对的map。
	 * @param keys 要获取值的键数组。
	 * @param defaultValue 如果所有key都不存在或对应的值为null时返回的默认值。
	 * @return 转换为float类型的值，如果所有key都不存在或对应的值为null，则返回默认值。
	 */
	public static <K,V>float getFirstFloat(Map<K, V> map, K[] keys, float defaultValue) {
	    return ObjectUtils.toFloat(
	            getFirst(map, keys), defaultValue);
	}
	
	/**
	 * 从Map中获取Integer值
	 * @param map 包含键值对的Map
	 * @param key 需要获取值的键
	 * @return 对应键的Integer值，如果键不存在或对应值无法转换为Integer，则返回null
	 */
	public static <K,V>Integer getInt(Map<K, V> map, K key) {
	    return ObjectUtils.toInt(
	            get(map, key));
	}

	/**
	 * 从Map中获取int值，如果键不存在或转换失败，则使用默认值
	 * @param map 包含键值对的Map
	 * @param key 需要获取值的键
	 * @param defaultValue 默认值
	 * @return 对应键的int值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>int getInt(Map<K, V> map, K key, int defaultValue) {
	    return ObjectUtils.toInt(
	            get(map, key), defaultValue);
	}

	/**
	 * 从Map中获取第一个Integer值
	 * @param map 包含键值对的Map
	 * @param keys 键数组，用于尝试获取值
	 * @return 第一个键对应的Integer值，如果键不存在或对应值无法转换为Integer，则返回null
	 */
	public static <K,V>Integer getFirstInt(Map<K, V> map, K[] keys) {
	    return ObjectUtils.toInt(
	            getFirst(map, keys));
	}

	/**
	 * 从Map中获取第一个int值，如果键不存在或转换失败，则使用默认值
	 * @param map 包含键值对的Map
	 * @param keys 键数组，用于尝试获取值
	 * @param defaultValue 默认值
	 * @return 第一个键对应的int值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>int getFirstInt(Map<K, V> map, K[] keys, int defaultValue) {
	    return ObjectUtils.toInt(
	            getFirst(map, keys), defaultValue);
	}
	
	/**
	 * 从Map中获取Long值，如果键不存在或转换失败，则返回null
	 * @param map 包含键值对的Map
	 * @param key 要获取值的键
	 * @return 对应键的Long值，如果键不存在或转换失败，则返回null
	 */
	public static <K,V>Long getLong(Map<K, V> map, K key) {
	    return ObjectUtils.toLong(
	            get(map, key));
	}

	/**
	 * 从Map中获取Long值，如果键不存在或转换失败，则返回默认值
	 * @param map 包含键值对的Map
	 * @param key 要获取值的键
	 * @param defaultValue 默认值
	 * @return 对应键的Long值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>long getLong(Map<K, V> map, K key, long defaultValue) {
	    return ObjectUtils.toLong(
	            get(map, key), defaultValue);
	}

	/**
	 * 从Map中获取第一个Long值，如果键不存在或转换失败，则返回null
	 * @param map 包含键值对的Map
	 * @param keys 键数组，用于尝试获取值
	 * @return 第一个键对应的Long值，如果键不存在或转换失败，则返回null
	 */
	public static <K,V>Long getFirstLong(Map<K, V> map, K[] keys) {
	    return ObjectUtils.toLong(
	            getFirst(map, keys));
	}

	/**
	 * 从Map中获取第一个long值，如果键不存在或转换失败，则使用默认值
	 * @param map 包含键值对的Map
	 * @param keys 键数组，用于尝试获取值
	 * @param defaultValue 默认值
	 * @return 第一个键对应的long值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>long getFirstLong(Map<K, V> map, K[] keys, long defaultValue) {
	    return ObjectUtils.toLong(
	            getFirst(map, keys), defaultValue);
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
	 * @return 对应键的boolean值，如果键不存在或转换失败，则返回null
	 */
	public static <K,V>Boolean getBoolean(Map<K, V> map, K key) {
	    // 使用ObjectUtils将get方法获取的值转换为Boolean
	    return ObjectUtils.toBoolean(
	            get(map, key));
	}

	/**
	 * 获取Boolean
	 * <p>
	 * 	当字符串为"true"或者"1"时返回true
	 * 	当字符串为"false"或者"0"时返回flase
	 * 	其余情况返回默认值
	 * </p>
	 *
	 * @param map map
	 * @param key key
	 * @param defaultValue 默认值
	 * @return 对应键的boolean值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>boolean getBoolean(Map<K, V> map, K key, boolean defaultValue) {
	    // 使用ObjectUtils将get方法获取的值转换为Boolean，如果转换失败则返回默认值
	    return ObjectUtils.toBoolean(
	            get(map, key), defaultValue);
	}

	/**
	 * 获取第一个Boolean
	 * <p>
	 * 	当字符串为"true"或者"1"时返回true
	 * 	当字符串为"false"或者"0"时返回flase
	 * 	其余情况返回null
	 * </p>
	 *
	 * @param map map
	 * @param keys keys
	 * @return 第一个键对应的boolean值，如果键不存在或转换失败，则返回null
	 */
	public static <K,V>Boolean getFirstBoolean(Map<K, V> map, K[] keys) {
	    // 使用ObjectUtils将getFirst方法获取的值转换为Boolean
	    return ObjectUtils.toBoolean(
	            getFirst(map, keys));
	}

	/**
	 * 获取第一个Boolean
	 * <p>
	 * 	当字符串为"true"或者"1"时返回true
	 * 	当字符串为"false"或者"0"时返回flase
	 * 	其余情况返回默认值
	 * </p>
	 *
	 * @param map map
	 * @param keys keys
	 * @param defaultValue 默认值
	 * @return 第一个键对应的boolean值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>boolean getFirstBoolean(Map<K, V> map, K[] keys, boolean defaultValue) {
	    // 使用ObjectUtils将getFirst方法获取的值转换为Boolean，如果转换失败则返回默认值
	    return ObjectUtils.toBoolean(
	            getFirst(map, keys), defaultValue);
	}
	
	/**
	 * 从Map中获取指定键的Date值
	 * <p>
	 * 	支持四种格式
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd HH:mm
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 *
	 * @param map 包含键值对的Map
	 * @param key 需要获取的键
	 * @return 对应键的Date值，如果键不存在或转换失败，则返回null
	 */
	public static <K,V>Date getDate(Map<K, V> map, K key) {
	    return ObjectUtils.toDate(
	            get(map, key));
	}

	/**
	 * 从Map中获取指定键的Date值，并指定默认值
	 * <p>
	 * 支持三种格式
	 *	 yyyy-MM-dd HH:mm:ss
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 *
	 * @param map 包含键值对的Map
	 * @param key 需要获取的键
	 * @param defaultValue 默认的Date值
	 * @return 对应键的Date值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>Date getDate(Map<K, V> map, K key, Date defaultValue) {
	    return ObjectUtils.toDate(
	            get(map, key), defaultValue);
	}

	/**
	 * 从Map中获取多个键中的第一个Date值
	 * <p>
	 * 	支持四种格式
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd HH:mm
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 *
	 * @param map 包含键值对的Map
	 * @param keys 需要尝试获取的键数组
	 * @return 第一个找到的Date值，如果键不存在或转换失败，则返回null
	 */
	public static <K,V>Date getFirstDate(Map<K, V> map, K[] keys) {
	    return ObjectUtils.toDate(
	            getFirst(map, keys));
	}

	/**
	 * 从Map中获取多个键中的第一个Date值，并指定默认值
	 * <p>
	 * 支持三种格式
	 *	 yyyy-MM-dd HH:mm:ss
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 *
	 * @param map 包含键值对的Map
	 * @param keys 需要尝试获取的键数组
	 * @param defaultValue 默认的Date值
	 * @return 第一个找到的Date值，如果键不存在或转换失败，则返回默认值
	 */
	public static <K,V>Date getFirstDate(Map<K, V> map, K[] keys, Date defaultValue) {
	    return ObjectUtils.toDate(
	            getFirst(map, keys), defaultValue);
	}
	
	/**
	 * 从给定的Map中移除指定的键
	 *
	 * @param <K> Map中键的类型
	 * @param <V> Map中值的类型
	 * @param map 要从中移除键的Map
	 * @param key 要移除的键
	 * @return 移除指定键后的Map如果输入的Map为null，则返回null
	 */
	public <K,V> Map<K,V> remove(Map<K, V> map, K key) {
	    // 检查输入的Map是否为null，如果为null则返回null
	    if(map == null) {
	        return null;
	    }
	    // 从Map中移除指定的键
	    map.remove(key);
	    // 返回移除指定键后的Map
	    return map;
	}
	
	/**
	 * 转换map中的key
	 * 
	 * @param src 需要进行key转换的源map
	 * @param convertMap 源key和转换的目标key的对应map
	 * @return 返回转换key后的map
	 */
	public static <K,V>Map<K,V> convertKeys(Map<K,V> src, Map<K,K> convertMap) {
	    // 遍历转换map的key集合
	    Iterator<K> itor = convertMap.keySet().iterator();
	    while(itor.hasNext()) {
	        // 获取当前遍历的源key
	        K key1 = itor.next();
	        // 获取源key对应的转换后的key
	        K key2 = convertMap.get(key1);
	        // 调用convertKey方法进行key的转换
	        convertKey(src, key1, key2);
	    }
	    // 返回转换key后的源map
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
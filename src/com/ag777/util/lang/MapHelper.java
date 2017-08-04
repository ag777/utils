package com.ag777.util.lang;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ag777.util.gson.GsonUtils;

/**
 * @author ag777
 * @Description 哈希表辅助类(废弃maputils)
 * Time: created at 2017/6/15. last modify at 2017/8/4.
 * Mark: 所有的复制方法都会根据原列表vector则会复制成vector，linkList复制成linklist,其余均复制成arrayList
 */
public class MapHelper<K,V> {

	private Map<K,V> map;
	
	/*------静态工具方法-----*/
	/**
	 * 获取map里key对应的值
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
	
	public static <K,V,T>T get(Map<K, V> map, K key) {
		return get(map, key, null);
	}
	
	/*------构造函数-----*/
	public MapHelper() {
		map = new HashMap<>();
	}
	
	public MapHelper(Map<K,V> map) {
		this.map = map;
	}
	
	public MapHelper(K key, V value) {
		map = new HashMap<>();
		map.put(key, value);
	}
	
	//--静态构造
	public static <K,V>MapHelper<K,V> parse(Map<K, V> map) {
		return new MapHelper<K,V>(map);
	}
	
	/**
	 * 通过json串构造对象(调用GsonUtils.toMap方法)，注意json串有可能转换失败
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static MapHelper<String, Object> fromJson(String json) throws Exception {
		try {
			Map<String, Object> temp = GsonUtils.get().toMap(json);
			if(temp == null)  {
				throw new Exception("JSON转换失败!");
			}
			return new MapHelper<String, Object>(temp);
		} catch(Exception ex) {
			throw ex ;
		}
	}
	
	/**
	 * 将List<Map> 中的键值从新组成map(不做类型转化判断，就是说结果类型要是对不上，强转报错请注意)
	 * @param list 源列表 List<Map>,为null返回空列表
	 * @param key_tilte 源List<Map>中key_tilte对应的值(不允许为null)作为结果Map中的key
	 * @param key_value 源List<Map>中key_value对应的值作为结果Map中的value
	 * @return
	 */
	public static MapHelper<String, Object> listMap(List<Map<String, Object>> list, String key_tilte, String key_value) {
		if(list == null) {
			return new MapHelper<>();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map<String, Object> item : list) {
			if(item.containsKey(key_tilte) && item.get(key_tilte) != null && item.containsKey(key_value)) {
				try {
					map.put(item.get(key_tilte).toString(), item.get(key_value));
				} catch(Exception ex) {
				}
			}
		}
		return new MapHelper<>(map);
	}
	
	/*------工具方法-----*/
	/**
	 * 获取map
	 * @return
	 */
	public Map<K,V> getMap() {
		return map;
	}
	
	/**
	 * 添加数据
	 * @param key
	 * @param value
	 * @return
	 */
	public MapHelper<K, V> put(K key, V value) {
		if(key != null) {
			map.put(key, value);
		}
		return this;
	}
	
	/**
	 * 从map中取值,中途出错或返回值为null时返回默认值
	 * @param key
	 * @param defaultValue 默认返回值
	 * @return
	 */
	public <T>T get(K key, T defaultValue) {
		return get(map, key, defaultValue);
	}
	
	/**
	 * 从map中取值
	 * @param key
	 * @return
	 */
	public <T>T get(K key) {
		return get(key, null);
	}
	
	/**
	 * 通过key提取字符串，如果类型不为会返回结果.toString()
	 * @param key
	 * @return
	 */
	public String getString(K key) {
		return getString(key, null);
	}
	
	/**
	 * 通过key提取字符串，如果类型不为会返回结果.toString()
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(K key, String defaultValue) {
		if(map.containsKey(key)) {
			V value = map.get(key);
			if(value == null) {
				return defaultValue;
			}
			if(value instanceof String) {
				return (String) value;
			} else {
				return value.toString();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 通过key提取int，如果类型不为Integer会尝试进行转换
	 * @param key
	 * @return 失败返回null
	 */
	public Integer getInteger(K key) {
		return getInteger(key, null);
	}
	
	/**
	 * 通过key提取int，如果类型不为Integer会尝试进行转换
	 * @param key
	 * @param defaultValue 默认值
	 * @return 失败返回默认值
	 */
	public Integer getInteger(K key, Integer defaultValue) {
		if(map.containsKey(key)) {
			V value = map.get(key);
			if(value == null) {
				return defaultValue;
			}
			if(value instanceof Integer) {
				return (int) value;
			} else {
				try {
					return Integer.parseInt(value.toString());
				} catch(Exception ex) {
					return defaultValue;
				}
			}
		}
		return defaultValue;
	}
	
	/**
	 * 通过key提取double，如果类型不为Double会尝试进行转换
	 * @param key
	 * @return 失败返回null
	 */
	public Double getDouble(K key) {
		return getDouble(key, null);
	}
	
	/**
	 * 通过key提取double，如果类型不为Double会尝试进行转换
	 * @param key
	 * @param defaultValue 默认值
	 * @return 失败返回默认值
	 */
	public Double getDouble(K key, Double defaultValue) {
		if(map.containsKey(key)) {
			V value = map.get(key);
			if(value == null) {
				return defaultValue;
			}
			if(value instanceof Double) {
				return (double) value;
			} else {
				try {
					return Double.parseDouble(value.toString());
				} catch(Exception ex) {
					return defaultValue;
				}
			}
		}
		return defaultValue;
	}
	
	/**
	 * 遍历map
	 * @param map
	 * @param viewer
	 */
	public static <K, V>void foreach(Map<K, V> map, Viewer<K, V> viewer) {
		Iterator<K> itor = map.keySet().iterator();
		while(itor.hasNext()) {
			K key = itor.next();
			V value = map.get(key);
			if(viewer.doView(key, value)) {	//返回true则停止遍历(参考安卓事件分发机制)
				break;
			}
		}
	}
	
	/**
	 * 删除map中符合要求的元素
	 * @param filter 返回true的时候删除
	 */
	public void remove(Filter<K, V> filter) {
		Iterator<K> itor = map.keySet().iterator();
		while(itor.hasNext()) {
			K key = itor.next();
			V value = map.get(key);
			if(filter.doFilter(key, value)) {
				itor.remove();	//安全删除
			}
		}
	}
	
	/**
	 * 编辑哈希表中的项,doEdit返回null则会删除该项
	 * @param editor
	 */
	public void edit(Editor<K, V> editor) {
		Iterator<K> itor = map.keySet().iterator();
		while(itor.hasNext()) {
			K key = itor.next();
			V value = map.get(key);
			V newValue = editor.doEdit(itor, key, value);
			if(newValue == null) {
				itor.remove();	//安全删除
			}else {
				map.put(key, newValue);
			}
		}
	}
	
	/*-------辅助类-----------*/
	public interface Viewer<K, V> {
		boolean doView(K key, V value);			//返回true则停止遍历(参考安卓事件分发机制)
	}
	public interface Filter<K, V> {
		boolean doFilter(K key, V value);	//匹配则返回true
	}
	public interface Editor<K ,V> {	//开放所有相关数据
		V doEdit(Iterator<K> it, K key, V value);
	}
}

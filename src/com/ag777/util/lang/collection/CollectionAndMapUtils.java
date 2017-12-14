package com.ag777.util.lang.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;


/**
 * 有关 <code>Collection和Map</code> 工具类。
 * 
 * @author ag777
 * @version create on 2017年06月15日,last modify at 2017年12月14日
 */
public class CollectionAndMapUtils {

	/**
	 * 创建数组
	 * <p>
	 * 	由于基本类型不能作为泛型，所以只好在外部自行强转了
	 * 	<p>
	 * 		CollectionAndMapUtils.newArray(int.class, 3) = [0,0,0];
	 * 	</p>
	 * </p>
	 * @param clazz
	 * @param length
	 * @return
	 */
	public static Object newArray(Class<?> clazz, int length) {
		return Array.newInstance(clazz, length);
	}
	
	public static <T>ArrayList<T> newArrayList() {
		return new ArrayList<>();
	}
	
	public static <T>LinkedList<T> newLinkedList() {
		return new LinkedList<>();
	}
	
	public static <T>Vector<T> newVector() {
		return new Vector<>();
	}
	
	public static <K,V>HashMap<K,V> newHashMap() {
		return new HashMap<>();
	}
	
	public static <K,V>Hashtable<K,V> newHashtable() {
		return new Hashtable<>();
	}
	
	public static <K,V>LinkedHashMap<K,V> newLinkedHashMap() {
		return new LinkedHashMap<>();
	}
	
	public static <E>HashSet<E> newHashSet() {
		return new HashSet<>();
	}
	
	public static <E>boolean isEmpty(Collection<E> collection) {
		if(collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static <T>boolean isEmpty(T[] array) {
		if(array == null || array.length == 0) {
			return true;
		}
		return false;
	}
	
	public static <K,V>boolean isEmpty(Map<K, V> map) {
		if(map== null || map.isEmpty()) {
			return true;
		}
		return false;
	}
}

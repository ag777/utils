package com.ag777.util.lang.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * 有关 <code>Collection和Map</code> 工具类。
 * 
 * @author ag777
 * @version create on 2017年06月15日,last modify at 2017年09月26日
 */
public class CollectionAndMapUtils {

	public static <T>List<T> newArrayList() {
		return new ArrayList<>();
	}
	
	public static <T>List<T> newLinkedList() {
		return new LinkedList<>();
	}
	
	public static <T>List<T> newVector() {
		return new Vector<>();
	}
	
	public static <K,V>Map<K,V> newHashMap() {
		return new HashMap<>();
	}
	
	public static <K,V>Map<K,V> newHashTable() {
		return new Hashtable<>();
	}
	
	public static <K,V>Map<K,V> newLinkedHashMap() {
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
}

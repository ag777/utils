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
 * @author ag777
 * @Description collection辅助类
 * Time: created at 2017/06/15. last modify at 2017/09/22.
 * Mark: 
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

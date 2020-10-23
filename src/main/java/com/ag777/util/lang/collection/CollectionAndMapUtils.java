package com.ag777.util.lang.collection;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 有关 <code>Collection和Map</code> 工具类。
 * 
 * @author ag777
 * @version create on 2017年06月15日,last modify at 2018年11月22日
 */
public class CollectionAndMapUtils {

	/**
	 * 创建数组
	 * <p>
	 * 	由于基本类型不能作为泛型，所以只好在外部自行强转了
	 * 	<p><pre>{@code
	 * 		CollectionAndMapUtils.newArray(int.class, 3) = [0,0,0];
	 * }</pre>
	 * 
	 * @param clazz clazz
	 * @param length length
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
	
	/**
	 * <p>
	 * 在并发访问的情景下，当需要修改元素时，不直接修改该容器，而是先复制一份副本，在副本上进行修改。修改完成之后，将指向原来容器的引用指向新的容器(副本容器)。<br>
	 * 但是这样使用开销会增大,所以只适合数据较少的情况
	 * </p>
	 * @return
	 */
	public static <T>CopyOnWriteArrayList<T> newCopyOnWriteArrayList() {
		return new CopyOnWriteArrayList<>();
	}
	
	public static <K,V>HashMap<K,V> newHashMap() {
		return new HashMap<>();
	}
	
	@Deprecated
	public static <K,V>Hashtable<K,V> newHashtable() {
		return new Hashtable<>();
	}
	
	/**
	 * <p>
	 * 采用分段锁机制,且读取基本不加锁,操作时效率大于hashtable
	 * </p>
	 * @return
	 */
	public static <K, V>ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<>();
	}
	
	public static <K,V>LinkedHashMap<K,V> newLinkedHashMap() {
		return new LinkedHashMap<>();
	}
	
	public static <E>HashSet<E> newHashSet() {
		return new HashSet<>();
	}
	
	/**
	 * <p>
	 * 因为Stack继承自Vector，所以Stack类是同步的，效率不高。官方一般建议这样使用ArrayDeque代替Stack<br>
	 * 建议使用栈时，用ArrayDeque的push和pop方法；<br>
	 * 使用队列时，使用ArrayDeque的add和remove方法。<br>
	 * ArrayDeque并不是一个固定大小的队列，每次队列满了以后就将队列容量扩大一倍
	 * </p>
	 * @return
	 */
	public static <T>ArrayDeque<T> newStack() {
		return new ArrayDeque<>();
	}
	
	/**
	 * 线程安全队列
	 * @return
	 */
	public static <T>ConcurrentLinkedQueue<T> newConcurrentLinkedQueue() {
		return new ConcurrentLinkedQueue<>();
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
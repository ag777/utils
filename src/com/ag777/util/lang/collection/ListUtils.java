package com.ag777.util.lang.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import com.ag777.util.lang.collection.interf.Comparator;
import com.ag777.util.lang.collection.interf.ListFilter;

/**
 * 有关 <code>List</code> 列表工具类。
 * 
 * @author ag777
 * @version create on 2017年09月22日,last modify at 2017年11月07日
 */
public class ListUtils {

	private ListUtils(){}
	
	private static <T>List<T> newList() {
		return newArrayList();
	}
	
	public static <T>ArrayList<T> newArrayList() {
		return CollectionAndMapUtils.newArrayList();
	}
	
	public static <T>LinkedList<T> newLinkedList() {
		return CollectionAndMapUtils.newLinkedList();
	}
	
	public static <T>Vector<T> newVector() {
		return CollectionAndMapUtils.newVector();
	}
	
	public static Object newArray(Class<?> clazz, int length) {
		return CollectionAndMapUtils.newArray(clazz, length);
	}
	
	public static <E>boolean isEmpty(Collection<E> collection) {
		return CollectionAndMapUtils.isEmpty(collection);
	}
	
	 /**
     * 拆分字符串组成列表
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		List<String> list = ListUtils.of("1,2,3", ",");
     *		结果为包含"1","2"和"3"的列表
     *		值得注意的是由于调用的是String.split(String regex)方法所以参数separator为正则表达式,注意字符串转义
     * </pre>
     * </p>
     * </p>
     */
	public static List<String> ofList(String src, String separator) {
		List<String> result = newList();
		if(src == null) {
			return result;
		}else {
			String[] group = src.split(separator);
			for (String item : group) {
				result.add(item);
			}
		}
		return result;
	}
	
	 /**
     * 数组转列表
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		List<Integer> list = ListUtils.toFinalList(new Integer[]{1,2,3});
     *		之后可以随意操作该列表
     * </pre>
     * </p>
     * </p>
     */
	public static <T>List<T> ofList(T[] items) {
		List<T> result = newList();
		if(items != null && items.length > 0) {
			for (T item : items) {
				result.add(item);
			}
		}
		return result;
	}
	
	 /**
     * 数组转定长列表(后续不可改变列表长度，否则报错)
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		List<Integer> list = ListUtils.toFinalList(new Integer[]{1,2,3});
     *		这时如果调用list.add(3)或者list.remove(2)就会报错
     * </pre>
     * </p>
     * </p>
     */
	public static <T>List<T> ofFinalList(T[] items) {
		return Arrays.asList(items);
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含一项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item) {
		List<T> list = newList();
		list.add(item);
		return list;
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含两项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item1, T item2) {
		List<T> list = of(item1);
		list.add(item2);
		return list;
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含三项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item1, T item2, T item3) {
		List<T> list = of(item1, item2);
		list.add(item3);
		return list;
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含四项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item1, T item2, T item3, T item4) {
		List<T> list = of(item1, item2, item3);
		list.add(item4);
		return list;
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含五项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item1, T item2, T item3, T item4, T item5) {
		List<T> list = of(item1, item2, item3, item4);
		list.add(item5);
		return list;
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含六项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item1, T item2, T item3, T item4, T item5, T item6) {
		List<T> list = of(item1, item2, item3, item4, item5);
		list.add(item6);
		return list;
	}
	
	/**
	 * 构建list
	 * <p>
	 * 	含七项
	 * </p>
	 * 
	 */
	public static <T>List<T> of(T item1, T item2, T item3, T item4, T item5, T item6, T item7) {
		List<T> list = of(item1, item2, item3, item4, item5, item6);
		list.add(item7);
		return list;
	}
	
	 /**
     * 数组转列表
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		现有key-value 为[{a:1,b:2},{a:2},{a:3}]的列表list
     *		ListUtils.of(list, "a");
     *		结果将是值[1,2,3] 的列表
     * </pre>
     * </p>
     * </p>
     */
	public static List<Object> ofList(List<Map<String, Object>> list, String key) {
		List<Object> resultList = newList();
		for (Map<String, Object> item : list) {
			try {
				Object obj = MapUtils.get(item, key);
				if(obj != null) {
					resultList.add(obj);
				}
			}catch(Exception ex) {
				//如果类型不匹配则不加入列表
			}
			
		}
		return resultList;
	}
	
	/**
     *  将list<Map>中每个map的第一个值(非空)整合成一个列表,适用于单列list
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		现有key-value 为[{a:1},{a:2},{a:3}]的列表list
     *		ListUtils.of(list);
     *		结果将是值[1,2,3] 的列表
     * </pre>
     * </p>
     * </p>
     */
	public static List<Object> ofList(List<Map<String, Object>> list) {
		List<Object> resultList = newList();
		for (Map<String, Object> item : list) {
			Iterator<String> itor = item.keySet().iterator();
			if(itor.hasNext()) {
				String key = itor.next();
				try {
					Object obj = item.get(key);
					resultList.add(obj);
				}catch(Exception ex) {
					//如果类型不匹配则不加入列表
				}
			}
		}
		return resultList;
	}
	
	 /**
     * 拆分字符串获取list
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		ListUtils.of(new Integer[]{1,2,3});
     *		ListUtils.toString(list,",");
     *		得到的结果是1,2,3
     * </pre>
     * </p>
     * </p>
     */
	public static <T>String toString(List<T> list, String separator) {
		if(CollectionAndMapUtils.isEmpty(list)) {	//列表为空则返回空字符串
			return "";
		}
		StringBuilder sb = null;
		for (T item : list) {
			if(sb == null) {
				sb = new StringBuilder();
			} else if(separator != null) {
				sb.append(separator);
			}
			if(item != null) {
				sb.append(item.toString());
			} else {
				sb.append("null");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 去重
	 */
    public static <T>List<T> distinct(List<T> list) {
        Set<T> set = new HashSet<T>();
        for (int i = list.size() - 1; i >= 0; i--) {	//倒序遍历，为了能删除数据
        	T item = list.get(i);
        	boolean a = set.add(item);
        	if (!a) {	//如果set中不能加入数据，说明重复了，需要移除
            	list.remove(item);
            }
        }
        return list;
    }
	
	/**
     * 删除值为null的项
     * <p>
     * 	用Iterator遍历
     * </p>
     */
    public static <T>List<T> removeNull(List<T> list) {
    	Iterator<T> itor = list.iterator();
    	while(itor.hasNext()) {
    		if(itor.next() == null) {
    			itor.remove();
    		}
    	}
        return list;
    }
	
    /**
     * 删除列表中某项元素，避免list<Integer> 的下标陷阱
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * List<Integer> list = newList();
		list.add(1);
		list.add(2);
		list.add(3);
		这时我们需要删除值为2的元素,于是我们调用
		list.remove(2);
		结果是1,2而不是预期的1,3
		通常情况下,我们需要用list.remove((Object)2);来得到预期的结果
     * </pre>
     * </p>
     * </p>
     */
    public static <T>List<T> remove(List<T> list, Object item) {
    	list.remove(item);
    	return list;
    }
    
    
    /**
     * 列表分段(将一个列表，每limit长度分一个列表并将这些新列表整合到一个列表里),多用于数据库批量插入拆分
     * <p>
     * 	该方法原用于解决一次性批量插入数据到数据库时数据量太大导致插入失败的异常，将列表数据分次插入
     * 例如：
     * </p>
     * 
     * <pre>
     * 		现有值为[1,2,3]的列表list,执行
     * 		ListUtils.splitList(list, 2);
     * 		将会得到值为[[1,2],[3]]的嵌套列表
     * 		值得注意的是如果传入的limit不为正数，则会抛出RuntimeException
     * </pre>
     * </p>
     * </p>
     */
	public static <T>List<List<T>> splitList(List<T> list, int limit) {
		if(limit <= 0) {
			throw new RuntimeException("参数limit必须大于0");
		} 
		List<List<T>> result = newList();
		int size = list.size()/limit+1;
		for (int i = 0; i < size; i++) {
			int min = limit*i;
			int max = limit*(i+1);
			max = max>list.size()?list.size():max;
			if(max > min) {
				List<T> item = list.subList(min, max);
				result.add(item);
			}
			
		}
		return result;
	}
	
	
	/**
     * 删除符合条件的列表项
     * 
     * <pre>
     * 		filter返回true时则会删除该项
     * </pre>
     * </p>
     * </p>
     */
	public static <T>List<T> removeByFilter(List<T> list, ListFilter<T> filter) {
		Iterator<T> itor = list.iterator();
		while(itor.hasNext()) {
			T item = itor.next();
			if(filter.dofilter(item)) {	//删除对应数据
				itor.remove();
			}
		}
		return list;
	}
	
	
	/**
     * 列表转List<Map<String, Object>>
     * <p>
     * 例如：
     * </p>
     * 
     * <pre>
     * 		现有值为[1,2,3]的列表list,执行
     * 		ListUtils.toMap(list, "a");
     * 		将会得到值为[{a:1},{a:2},{a:3}]的列表
     * 		值得注意的是如果传入的key为null的话返回也会是null,传入的list为null返回的将是空列表
     * </pre>
     * </p>
     * </p>
     */
	public static <T>List<Map<String, Object>> toListMap(List<T> list, String key) {
		if(key == null) {	//键为null的情况
			return null;
		}
		if(list == null) {
			return newList();
		}
		
		List<Map<String, Object>> newList =newList();
		for (T item : list) {
			Map<String,	Object> map = CollectionAndMapUtils.newHashMap();
			map.put(key, item);
			newList.add(map);
		}
		return newList;
	} 
	
	//--复制
	/**
	 * 将列表转化为数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T>T[] toArray(List<T> list) {
		return (T[]) list.toArray(new Object[list.size()]);
	}
	
	/**
	 * 深度拷贝
	 * @return
	 */
	public static <T>List<T> copy(List<T> list) {
		List<T> newList = emptyToCopy(list);
		Collections.addAll(newList,  toArray(list)); 
		//Collections.copy(newList, list);	//深拷贝，不光拷贝的是src的元素（引用），src内每个元素的所指向的对象都进行一次拷贝。即是两个list的每个元素所指向的不是同一内存
		return newList;
	}
	
	//--排序
	/**
	 * 列表排序
	 * @param comparator	比较器 ,胜利的一方(isWinner)排名向上(排在前面)
	 */
	public static <T>List<T> sort(List<T> list, final Comparator<T> comparator) {
		Collections.sort(list,new java.util.Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				if(comparator.isWinner(o1, o2)) {
					return -1;	//排名往上升
				}
				return 1;
			}
		});
		return list;
	}
	
	/**
	 * 列表倒序排列
	 */
	public static <T>List<T> sortReverse(List<T> list) {
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * 列表随机排序
	 */
	public static <T>List<T> sortShuffle(List<T> list) {
		Collections.shuffle(list);
		return list;
	}
	
	/**
	 * 获取一个元素在数组中的位置
	 * <p>
	 * 	用equals实现比较<br/>
	 * 	通过调用isPresent()方法直接获取是否在数组中,不需要判断值是否大于-1
	 * </p>
	 * @param array
	 * @param item
	 * @return
	 */
	public static <T>Optional<Integer> inArray(T[] array, Object item) {
		if(array == null || item==null) {
			return Optional.empty();
		}
		for(int i=0;i<array.length;i++) {
			if(item.equals(array[i])) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}
	
	/*----内部方法----*/
	/**
	 * 获取镜像的空列表，支持arrayList,vector和linklist
	 * @param list
	 * @return
	 */
	private static <T>List<T> emptyToCopy(List<T> list) {
		if(list == null) {	//所有方法都要做空指针判断
			return null;
		}
		List<T> newList;
		if(list instanceof Vector) {	//继承于线程安全列表
			newList = CollectionAndMapUtils.newVector();
		}else if(list instanceof LinkedList) {
			newList = CollectionAndMapUtils.newLinkedList();
		}else {
			newList = newArrayList();
		}
		return newList;
	}
}

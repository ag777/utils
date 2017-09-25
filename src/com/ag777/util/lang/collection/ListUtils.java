package com.ag777.util.lang.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.ag777.util.lang.collection.interf.Comparator;
import com.ag777.util.lang.collection.interf.ListFilter;

/**
 * @author ag777
 * @Description 列表工具类
 * Time: created at 2017/09/22. last modify at 2017/09/22.
 * Mark: 
 */
public class ListUtils {

	private ListUtils(){}
	
	public <T>List<T> newArrayList() {
		return CollectionAndMapUtils.newArrayList();
	}
	
	public <T>List<T> newVector() {
		return CollectionAndMapUtils.newVector();
	}
	
	/**
	 * 格式化字符串(a,b,c)转化为列表
	 * @param src
	 * @param separator
	 * @return
	 */
	public static List<String> toList(String src, String separator) {
		List<String> result = CollectionAndMapUtils.newArrayList();
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
	 * @param items
	 * @return
	 */
	public static <T>List<T> toList(T[] items) {
		List<T> result = CollectionAndMapUtils.newArrayList();
		if(items != null && items.length > 0) {
			for (T item : result) {
				result.add(item);
			}
		}
		return result;
	}
	
	/**
	 * 数组转定长列表(后续不可改变列表长度，否则报错)
	 * @param items
	 * @return
	 */
	public static <T>List<T> toFinalList(T[] items) {
		return Arrays.asList(items);
	}
	
	/**
	 * 将list<Map>中每个map的key对应的值(非空)整合成一个列表,适用于单列list
	 * @param list
	 * @param key
	 * @return
	 */
	public static List<Object> toList(List<Map<String, Object>> list, String key) {
		List<Object> resultList = CollectionAndMapUtils.newArrayList();
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
	 * 将list<Map>中每个map的第一个值(非空)整合成一个列表,适用于单列list
	 * @param list	列表
	 * @return 
	 */
	public static List<Object> toList(List<Map<String, Object>> list) {
		List<Object> resultList = CollectionAndMapUtils.newArrayList();
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
	 * @return
	 */
    public <T>List<T> distinct(List<T> list) {
        Set<T> set = new HashSet<T>();
        for (int i = list.size() - 1; i > 0; i--) {	//倒序遍历，为了能删除数据
        	T item = list.get(i);
        	if (!set.add(item)) {	//如果set中不能加入数据，说明重复了，需要移除
            	list.remove(item);
            }
        }
        return list;
    }
	
	/**
     * 删除值为null的项
     * @return
     */
    public <T>List<T> removeNull(List<T> list) {
        for (int i = list.size() - 1; i > 0; i--) {	//倒序遍历，为了能删除数据
        	T item = list.get(i);
        	if (item == null) {	//数据项为null则删除
            	list.remove(item);
            }
        }
        return list;
    }
	
	 /**
	 * 列表分段(将一个列表，每limit长度分一个列表并将这些新列表整合到一个列表里),多用于数据库批量插入拆分
	 * @param limit 每段数组的长度 > 0
	 * @return
	 */
	public <T>List<List<T>> splitList(List<T> list, int limit) {
		List<List<T>> result = CollectionAndMapUtils.newArrayList();
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
	 * 倒序删除列表项
	 * @param list
	 * @param filter
	 * @return
	 */
	public static <T>List<T> remove(List<T> list, ListFilter<T> filter) {
		//倒序循环删除对应数据
		for (int i = list.size() - 1; i > 0; i--) {
			T item = list.get(i);
			if(!filter.dofilter(item)) {	//删除对应数据
				list.remove(i);
			}
		}
		return list;
	}
	
	
	/**
	 * 列表转map
	 * @param list
	 * @param keys 标题列表,null时返回null
	 * @return {标题:值}
	 */
	public static <T>Map<String, Object> toMap(List<T> list, List<String> keys) {
		if(keys == null) {	//键为null的情况
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		int i = 0;
		for (String key : keys) {
			if(i < list.size()) {
				map.put(key, list.get(i));
			}else {	//超过值列表的大小
				map.put(key, null);
			}
			i++;
		}
		return map;
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
			newList = CollectionAndMapUtils.newArrayList();
		}
		return newList;
	}
}

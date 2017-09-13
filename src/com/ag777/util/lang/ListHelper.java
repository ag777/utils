package com.ag777.util.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author ag777
 * @Description 列表工具类(废弃ListUtils)
 * Time: created at 2017/6/15. last modify at 2017/9/13.
 * Mark: 所有的复制方法都会根据原列表vector则会复制成vector，linkList复制成linklist,其余均复制成arrayList
 */
public class ListHelper<T> {

	private List<T> list;
	
	/*------构造函数-----*/
	public ListHelper() {
		list = new ArrayList<>();
	}
	
	public ListHelper(List<T> list) {
		this.list = list;
	}
	
	//--静态构造
	public static <T>ListHelper<T> parse(List<T> list) {
		return new ListHelper<>(list);
	}
	
	public static <T>ListHelper<T> addItem(T item) {
		return new ListHelper<T>().add(item);
	}
	
	@SafeVarargs
	public static <T>ListHelper<T> addAllItem(T... items) {
		List<T> list = new ArrayList<>();
		if(items != null) {
			for (T item : items) {
				if(item != null) {
					list.add(item);
				}
			}
		}
		return new ListHelper<>(list);
	}
	
	/**
	 * 格式化字符串(a,b,c)转化为列表
	 * @param src
	 * @param separator
	 * @return
	 */
	public static ListHelper<String> split(String src, String separator) {
		if(src == null) {
			return new ListHelper<>();
		}else {
			return ListHelper.addAllItem(src.split(separator));
		}
	}
	
	/**
	 * 将list<Map>中每个map的第一个值(非空)整合成一个列表,适用于单列list
	 * @param list	列表
	 * @param filter 过滤器 return true表示加入列表
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static <T>ListHelper<T> listMap(List<Map<String, Object>> list, Filter<T> filter) {
		List<T> resultList = new LinkedList<T>();
		for (Map<String, Object> item : list) {
			Iterator<String> itor = item.keySet().iterator();
			if(itor.hasNext()) {
				String key = itor.next();
				try {
					T obj = (T) item.get(key);
					if(obj != null && (filter == null || filter.dofilter(obj))) {
						resultList.add(obj);
					}
				}catch(Exception ex) {
					//如果类型不匹配则不加入列表
				}
			}
		}
		return new ListHelper<>(resultList);
	}
	
	/**
	 * 将list<Map>中每个map的第一个值(非空)整合成一个列表,适用于单列list
	 * @param list	列表
	 * @param filter 过滤器 return true表示加入列表
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static <T>ListHelper<T> listMap(List<Map<String, Object>> list, String key, Filter<T> filter) {
		List<T> resultList = new LinkedList<T>();
		for (Map<String, Object> item : list) {
			try {
				T obj = (T) item.get(key);
				if(obj != null && (filter == null || filter.dofilter(obj))) {
					resultList.add(obj);
				}
			}catch(Exception ex) {
				//如果类型不匹配则不加入列表
			}
			
		}
		return new ListHelper<>(resultList);
	}
	
	/*------工具方法-----*/
	//--静态方法
	public static <T>String toString(List<T> list, String separator) {
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
		return sb!=null?sb.toString():"";
	}
	
	//--非静态方法
	/**
	 * 获取list
	 * @return
	 */
	public List<T> getList() {
		return list;
	}
	
	/**
	 * 取得数据
	 * @param index
	 * @return
	 */
	public T get(int index) {
		return list.get(index);
	}
	
	/**
	 * 添加数据
	 * @param item
	 * @return
	 */
	public ListHelper<T> add(T item) {
		if(item != null) {
			list.add(item);
		}
		return this;
	}
	
	/**
	 * 批量添加数据
	 * @param items
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListHelper<T> addAll(T... items) {
		if(items != null) {
			for (T item : items) {
				if(item != null) {
					list.add(item);
				}
				
			}
		}
		
		return this;
	}
	
	/**
	 * 批量添加数据
	 * @param list
	 * @return
	 */
	public ListHelper<T> addAll(List<T> list) {
		list.addAll(list);
		return this;
	}
	
	/**
	 * 删除数据
	 * @param filter
	 * @return
	 */
	public ListHelper<T> remove(Filter<T> filter) {
		
		//倒序循环删除对应数据
		for (int i = list.size() - 1; i > 0; i--) {
			T item = list.get(i);
			if(!filter.dofilter(item)) {	//删除对应数据
				list.remove(i);
			}
		}
		return this;
	}
	
	/**
	 * 修改列表项
	 * @param list
	 * @param editor
	 * @return
	 */
	public ListHelper<T> edit(Editor<T> editor) {
		//只修改,正序遍历就够了,这里为了适应所有情况(比如List<Integer>),不能用foreach(不知道当前位于列表中的那一项)
		for (int i = 0; i < list.size(); i++) {
			T item = list.get(i);
			list.set(i, editor.doEdit(item));	//保证修改测试包括int类型
		}
		return this;
	}
	
	/**
	 * 去重
	 * @return
	 */
    public ListHelper<T> distinct() {
        Set<T> set = new HashSet<T>();
        for (int i = list.size() - 1; i > 0; i--) {	//倒序遍历，为了能删除数据
        	T item = list.get(i);
        	if (!set.add(item)) {	//如果set中不能加入数据，说明重复了，需要移除
            	list.remove(item);
            }
        }
        return this;
    }
    
    /**
     * 删除值为null的项
     * @return
     */
    public ListHelper<T> removeNull() {
        for (int i = list.size() - 1; i > 0; i--) {	//倒序遍历，为了能删除数据
        	T item = list.get(i);
        	if (item == null) {	//数据项为null则删除
            	list.remove(item);
            }
        }
        return this;
    }
    
    /**
	 * 列表分段(将一个列表，每limit长度分一个列表并将这些新列表整合到一个列表里),多用于数据库批量插入拆分
	 * @param limit 每段数组的长度 > 0
	 * @return
	 */
	public List<List<T>> splitList(int limit) {
		List<List<T>> result = new ArrayList<List<T>>();
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
	 * 将列表转化为数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		return (T[]) list.toArray(new Object[list.size()]);
	}
	
	/**
	 * 深度拷贝
	 * @return
	 */
	public ListHelper<T> copy() {
		List<T> newList = emptyToCopy(list);
		Collections.addAll(newList,  toArray()); 
		//Collections.copy(newList, list);	//深拷贝，不光拷贝的是src的元素（引用），src内每个元素的所指向的对象都进行一次拷贝。即是两个list的每个元素所指向的不是同一内存
		return new ListHelper<>(newList);
	}
	
	//--返回不为本身
	/**
	 * list外部再包一层list
	 * @return
	 */
	public ListHelper<List<T>> toList() {
		List<List<T>> list2 = new ArrayList<>();
		list2.add(list);
		return new ListHelper<List<T>>(list2);
	}
	
	/**
	 * 格式化为字符串
	 * @param src
	 * @param separator
	 * @return
	 */
	public String toString(String separator) {
		return toString(list, separator);
	}
	
	/**
	 * 列表转map
	 * @param keys	标题列表,null时返回null
	 * @return {标题:值}
	 */
	public Map<String, Object> toMap(List<String> keys) {
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
	
	//--排序
	/**
	 * 列表排序
	 * @param comparator	比较器 ,胜利的一方(isWinner)排名向上(排在前面)
	 */
	public ListHelper<T> sort(final Comparator<T> comparator) {
		Collections.sort(list,new java.util.Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				if(comparator.isWinner(o1, o2)) {
					return -1;	//排名往上升
				}
				return 1;
			}
		});
		return this;
	}
	
	/**
	 * 列表倒序排列
	 */
	public ListHelper<T> sortReverse() {
		Collections.reverse(list);
		return this;
	}
	
	/**
	 * 列表随机排序
	 */
	public ListHelper<T> sortShuffle() {
		Collections.shuffle(list);
		return this;
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
			newList = new Vector<T>();
		}else if(list instanceof LinkedList) {
			newList = new LinkedList<T>();
		}else {
			newList = new ArrayList<T>();
		}
		return newList;
	}
	
	/*------------辅助类--------------*/
	/**
	 * 过滤器/拦截器
	 * @author ag777
	 * @param <T>
	 */
	public interface Filter<T> {
		boolean dofilter(T item);	//匹配则返回true
	}
	
	/**
	 * 编辑器
	 * @author ag777
	 * @param <T>
	 */
	public interface Editor<T> {
		T doEdit(T item);
	}
	
	/**
	 * 比较器
	 * @author ag777
	 * @param <T>
	 */
	public interface Comparator<T> {
		boolean isWinner(T o1, T o2);	//胜利者排前面
	}
	
}

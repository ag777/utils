package com.ag777.util.lang.collection.interf;

/**
 * 列表过滤类。
 * 
 * @author ag777
 * @version create on 2017年09月22日,last modify at 2017年09月26日
 */
public interface ListFilter<T> {
	boolean dofilter(T item);	//匹配则返回true
}

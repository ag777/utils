package com.ag777.util.lang.collection.interf;

public interface ListFilter<T> {
	boolean dofilter(T item);	//匹配则返回true
}

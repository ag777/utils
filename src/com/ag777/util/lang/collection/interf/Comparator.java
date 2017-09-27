package com.ag777.util.lang.collection.interf;

/**
 * 比较器。
 * 
 * @author ag777
 * @version 
 */
public interface Comparator<T> {
	boolean isWinner(T o1, T o2);	//胜利者排前面
}

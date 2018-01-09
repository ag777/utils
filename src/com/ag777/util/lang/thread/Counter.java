package com.ag777.util.lang.thread;

/**
 * 线程安全计数器
 * 
 * @author ag777
 * @version  create on 2017年11月1日,last modify at 2018年01月09日
 */
public class Counter {
	private long i;
	public Counter(int i) {
		this.i = i;
	}
	
	public Counter(long i) {
		this.i = i;
	}
	
	public long add(int b) {
		synchronized (Counter.class) {
			i = i+b;
			long n = i;
			return n;
		}
	}
	
	public long add() {
		return add(1);
	}
	
	public long subtract(int b) {
		synchronized (Counter.class) {
			i = i-b;
			long n = i;
			return n;
		}
	}
	
	public long subtract() {
		return subtract(1);
	}
	
	public long longValue() {
		return i;
	}
	
	public int intValue() {
		return Long.valueOf(i).intValue();
	}
}

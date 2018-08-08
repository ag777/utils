package com.ag777.util.lang.thread;

import java.util.concurrent.atomic.LongAdder;

/**
 * 线程安全计数器
 * 
 * @author ag777
 * @version  create on 2017年11月1日,last modify at 2018年08月08日
 */
public class Counter {
	private LongAdder l;
	
	public Counter(long i) {
		this.l = new LongAdder();
		this.l.add(i);
	}
	
	public void add(int b) {
		l.add(b);
	}
	
	public long addForNum(int b) {
		synchronized (Counter.class) {
			add(b);
			return l.longValue();
		}
	}
	
	public void add() {
		add(1);
	}
	
	public long addForNum() {
		return addForNum(1);
	}
	
	
	public long longValue() {
		return l.longValue();
	}
	
	public int intValue() {
		return l.intValue();
	}
}

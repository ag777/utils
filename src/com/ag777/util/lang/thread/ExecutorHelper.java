package com.ag777.util.lang.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 线程池ExecutorHelper辅助类
 * <p>
 * 简单包装,起到例子类的作用
 * </p>
 * 
 * @author ag777
 * @version  create on 2017年10月10日,last modify at 2017年10月10日
 */
public class ExecutorHelper {

	private ExecutorService pool;
	
	public ExecutorHelper(int size) {
		pool = Executors.newFixedThreadPool(size);
	}
	
	public void add(Runnable command) {
		pool.execute(command);
	}
	
	public <T>Future<T> add(Callable<T> task) {
		return pool.submit(task);
	}
	
	/**
	 * 等待子线程都结束
	 * <p>
	 * 	调用此方法后线程池不再接受新的任务,之后每100毫秒检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
	 * </p>
	 * @throws InterruptedException
	 */
	public void waitFor() throws InterruptedException {
		waitFor(100, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 等待子线程都结束
	 * <p>
	 * 	调用此方法后线程池不再接受新的任务,之后每根据参数指定的时间间隔检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
	 * </p>
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException
	 */
	public void waitFor(long timeout, TimeUnit unit) throws InterruptedException {
		pool.shutdown();
		while(!pool.awaitTermination(timeout, unit)) {	//如果结束则关闭线程池
		}
	}
			
	/**
	 * 释放资源
	 */
	public void dispose() {
		pool.shutdown();
		pool = null;
	}
	
}

package com.ag777.util.lang.thread;

import java.util.concurrent.*;

import com.ag777.util.lang.Console;
import com.ag777.util.lang.interf.Disposable;

/**
 * 回调线程池CompletionService辅助类
 * 
 * @author ag777
 * @version  create on 2018年08月03日,last modify at 2020年07月31日
 */
public class CompletionServiceHelper<T> implements Disposable {
	private ExecutorService pool;
	private CompletionService<T> completionService;
	
	
	public CompletionServiceHelper(int poolSize) {
		pool = Executors.newFixedThreadPool(poolSize);
		completionService = new ExecutorCompletionService<T>(pool);
	}
	
	public CompletionServiceHelper(ExecutorService pool) {
		this.pool = pool;
		completionService = new ExecutorCompletionService<T>(pool);
	}
	
	public ExecutorService getExecutorService() {
		return pool;
	}


	public CompletionService<T> getCompletionService() {
		return completionService;
	}


	public CompletionServiceHelper<T> add(Callable<T> task) {
		completionService.submit(task);
		return this;
	}

	public CompletionServiceHelper<T> add(Runnable task, T result) {
		completionService.submit(task, result);
		return this;
	}

	/**
	 * <p>阻塞线程，直至获取到结果
	 * @return 一个执行完成的任务
	 * @throws InterruptedException 等待中断
	 */
	public Future<T> take() throws InterruptedException {
		return completionService.take();
	}

	/**
	 * <p>不会阻塞线程，如果当前没有任务结束，则返回null
	 * @return 一个执行完成的任务
	 */
	public Future<T> poll() {
		return completionService.poll();
	}

	/**
	 * <p>阻塞线程一段时间，如果当前没有任务结束，则返回null
	 * @param timeout 等待时间
	 * @param timeunit 等待的时间单位
	 * @return 一个执行完成的任务
	 * @throws InterruptedException 等待中断
	 */
	public Future<T> poll(long timeout, TimeUnit timeunit) throws InterruptedException {
		return completionService.poll(timeout, timeunit);
	}
	
	/**
	 * 释放资源
	 */
	@Override
	public void dispose() {
		if(pool == null) {
			return;
		}
		pool.shutdownNow();
		pool = null;
	}
	
	/**
	 * 等待任务执行结束并且关闭线程池
	 */
	public void waitForDispose() {
		if(pool == null) {
			return;
		}
		try {
			waitFor();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			dispose();
		}
	}
	
	/**
	 * 等待任务执行结束并且关闭线程池
	 */
	public void waitForDisposeWithException() throws InterruptedException {
		if(pool == null) {
			return;
		}
		try {
			waitFor();
		} catch (InterruptedException e) {
			throw e;
		} finally {
			dispose();
		}
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
	private void waitFor(long timeout, TimeUnit unit) throws InterruptedException {
		pool.shutdown();
		try {
			while(!pool.awaitTermination(timeout, unit)) {	//如果结束则关闭线程池
			}
		} catch (InterruptedException e) {
			throw e;
		}
	}
	
	/**
	 * 等待子线程都结束
	 * <p>
	 * 	调用此方法后线程池不再接受新的任务,之后每100毫秒检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
	 * </p>
	 * @throws InterruptedException
	 */
	private void waitFor() throws InterruptedException {
		waitFor(100, TimeUnit.MILLISECONDS);
	}

	/**
	 * 详见线程池的使用方法
	 */
	public void shutdown() {
		if(pool != null) {
			pool.shutdown();
		}
	}

	/**
	 * 详见线程池的使用方法
	 */
	public void shutdownNow() {
		if(pool != null) {
			pool.shutdownNow();
		}
	}

	/**
	 * 详见线程池的使用方法
	 * @return 是否执行过shutdown
	 */
	public boolean isshutdown() {
		if(pool != null) {
			return pool.isShutdown();
		}
		return true;
	}

	/**
	 * 详见线程池的使用方法
	 * @return 是否完全停止
	 */
	public boolean isTerminated() {
		if(pool != null) {
			return pool.isTerminated();
		}
		return true;
	}
}

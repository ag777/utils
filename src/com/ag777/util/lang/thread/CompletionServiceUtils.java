package com.ag777.util.lang.thread;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.ag777.util.lang.Console;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.interf.Disposable;

/**
 * 回调线程池CompletionService辅助类
 * 
 * @author ag777
 * @version  create on 2018年08月03日,last modify at 2018年08月06日
 */
public class CompletionServiceUtils<T> implements Disposable {
	private ExecutorService pool;
	private CompletionService<T> completionService;
	private long taskCount = 0;
	
	public CompletionServiceUtils(int poolSize) {
		pool = Executors.newFixedThreadPool(poolSize);
		completionService = new ExecutorCompletionService<T>(pool);
	}
	
	public CompletionServiceUtils<T> add(Callable<T> task) {
		completionService.submit(task);
		taskCount++;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public CompletionServiceUtils<T> add(Callable<T>... tasks) {
		if(tasks == null) {
			return this;
		}
		for (Callable<T> task : tasks) {
			add(task);
		}
		return this;
	}
	
	public boolean hasNext() {
		return taskCount>0;
	}
	
	/**
	 * 获取所有结果
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public synchronized List<T> takeAll() throws InterruptedException, ExecutionException {
		List<T> list = ListUtils.newArrayList();
		for(;taskCount>0;taskCount--) {
			T result = completionService.take().get();
			list.add(result);
		}
		taskCount = 0;
		return list;
	}
	
	/**
	 * 获取1个执行完成的结果
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public synchronized T take() throws InterruptedException, ExecutionException {
		if(taskCount==0) {
			return null;
		}
		T result = completionService.take().get();
		taskCount--;
		return result;
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
	public void waitForDisposeWithException() {
		if(pool == null) {
			return;
		}
		try {
			waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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
//			e.printStackTrace();
			Console.err("等待线程池关闭失败");
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
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CompletionServiceUtils<Integer> u = new CompletionServiceUtils<>(5);
		for (int i = 0; i < 10; i++) {
			int n = i;
			u.add(()->{
				Thread.sleep(1000);
				return n;
			});
		}
		Console.log(u.takeAll());
		u.waitForDispose();
	}
}

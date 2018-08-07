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
 * @version  create on 2018年08月03日,last modify at 2018年08月07日
 */
public class CompletionServiceHelper<T> implements Disposable {
	private ExecutorService pool;
	private CompletionService<T> completionService;
	private long taskCount = 0;
	
	/**
	 * 执行任务返回任务结果列表
	 * @param taskList 任务列表
	 * @param poolSize 同时执行的任务数量
	 * @param errHandler 异常捕获
	 * @return
	 */
	public static <T>List<T> task(List<Callable<T>> taskList, int poolSize, ErrHandler<T> errHandler) {
		CompletionServiceHelper<T> helper = new CompletionServiceHelper<>(poolSize);
		List<T> list = helper.addAll(taskList).takeAll(errHandler);
		helper.waitForDispose();
		return list;
	}
	
	
	public CompletionServiceHelper(int poolSize) {
		pool = Executors.newFixedThreadPool(poolSize);
		completionService = new ExecutorCompletionService<T>(pool);
	}
	
	public CompletionServiceHelper<T> add(Callable<T> task) {
		completionService.submit(task);
		taskCount++;
		return this;
	}
	
	public CompletionServiceHelper<T> addAll(List<Callable<T>> taskList) {
		if(ListUtils.isEmpty(taskList)) {
			return this;
		}
		for (Callable<T> task : taskList) {
			add(task);
		}
		return this;
	}
	
	public boolean hasNext() {
		return taskCount>0;
	}
	
	/**
	 * 获取所有结果
	 * <p>
	 * 如果单个任务异常用handler捕获,并将返回添加至结果列表
	 * </p>
	 * @param errHandler
	 * @return
	 */
	public synchronized List<T> takeAll(ErrHandler<T> errHandler) {
		List<T> list = ListUtils.newArrayList();
		for(;taskCount>0;taskCount--) {
			T result = null;
			try {
				result = completionService.take().get();
			} catch (InterruptedException | ExecutionException e) {
				if(errHandler != null) {
					result = errHandler.onErr(e, list.size());	//这里取巧,当前列表大小(未加上新元素)就是新元素下标
				}
			}
			list.add(result);
		}
		taskCount = 0;
		return list;
	}
	
	/**
	 * 获取所有结果
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public synchronized List<T> takeAllWithException() throws InterruptedException, ExecutionException {
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
	
	public static interface ErrHandler<T> {
		public T onErr(Throwable throwable, int index);
	}
}

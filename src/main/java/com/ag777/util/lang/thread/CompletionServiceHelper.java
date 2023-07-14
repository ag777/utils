package com.ag777.util.lang.thread;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 回调线程池CompletionService辅助类
 * 
 * @author ag777
 * @version  create on 2018年08月03日,last modify at 2023年07月14日
 */
public class CompletionServiceHelper<T, V> {
	private ExecutorService pool;
	private CompletionService<T> completionService;
	private final Map<Future<T>, V> taskInfoMap;

	public CompletionServiceHelper(int poolSize) {
		this(Executors.newFixedThreadPool(poolSize));
	}
	
	public CompletionServiceHelper(ExecutorService pool) {
		this.pool = pool;
		completionService = new ExecutorCompletionService<>(pool);
		taskInfoMap = new ConcurrentHashMap<>(1);
	}
	public ExecutorService getExecutorService() {
		return pool;
	}


	public CompletionService<T> getCompletionService() {
		return completionService;
	}

	public int getTaskCount() {
		return taskInfoMap.size();
	}

	public CompletionServiceHelper<T, V> submit(Callable<T> task, V bindData) {
		Future<T> myTask = completionService.submit(task);
		if (bindData != null) {
			taskInfoMap.put(myTask, bindData);
		}
		return this;
	}

	public CompletionServiceHelper<T, V> submit(Runnable task, T result, V bindData) {
		Future<T> myTask = completionService.submit(task, result);
		if (bindData != null) {
			taskInfoMap.put(myTask, bindData);
		}
		return this;
	}

	/**
	 * <p>阻塞线程，直至获取到结果
	 * @return 一个执行完成的任务
	 * @throws InterruptedException 等待中断
	 */
	public Task<T, V> take() throws InterruptedException {
		Future<T> task = completionService.take();
		return new Task<>(task, taskInfoMap.remove(task));
	}

	/**
	 * <p>不会阻塞线程，如果当前没有任务结束，则返回null
	 * @return 一个执行完成的任务
	 */
	public Task<T, V> poll() {
		Future<T> task = completionService.poll();
		if (task == null) {
			return null;
		}
		return new Task<>(task, taskInfoMap.remove(task));
	}

	/**
	 * <p>阻塞线程一段时间，如果当前没有任务结束，则返回null
	 * @param timeout 等待时间
	 * @param timeunit 等待的时间单位
	 * @return 一个执行完成的任务
	 * @throws InterruptedException 等待中断
	 */
	public Task<T, V> poll(long timeout, TimeUnit timeunit) throws InterruptedException {
		Future<T> task = completionService.poll(timeout, timeunit);
		if (task == null) {
			return null;
		}
		return new Task<>(task, taskInfoMap.remove(task));
	}

	/**
	 * 取消剩余任务
	 * @param mayInterruptIfRunning 是否强制中断
	 */
	public void cancel(boolean mayInterruptIfRunning) {
		for (Future<T> task : taskInfoMap.keySet()) {
			task.cancel(mayInterruptIfRunning);
		}
	}
	
	/**
	 * 释放资源
	 */
	public void dispose() {
		if(pool == null) {
			return;
		}
		pool.shutdownNow();
		pool = null;
		completionService = null;
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
		} finally {
			dispose();
		}
	}
	
	/**
	 * 等待子线程都结束
	 * <p>
	 * 	调用此方法后线程池不再接受新的任务,之后每根据参数指定的时间间隔检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
	 * </p>
	 * @param timeout timeout
	 * @param unit unit
	 * @throws InterruptedException InterruptedException
	 */
	private void waitFor(long timeout, TimeUnit unit) throws InterruptedException {
		pool.shutdown();
		while(!pool.awaitTermination(timeout, unit)) {	//如果结束则关闭线程池
		}
	}
	
	/**
	 * 等待子线程都结束
	 * <p>
	 * 	调用此方法后线程池不再接受新的任务,之后每100毫秒检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
	 * </p>
	 * @throws InterruptedException InterruptedException
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
	public boolean isShutdown() {
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

	public static class Task<T, V> implements Future<T>{
		private Future<T> task;
		private V data;
		public Task(Future<T> task, V data) {
			this.task = task;
			this.data = data;
		}

		public V getData() {
			return data;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return task.cancel(mayInterruptIfRunning);
		}

		@Override
		public boolean isCancelled() {
			return task.isCancelled();
		}

		@Override
		public boolean isDone() {
			return task.isDone();
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			return task.get();
		}

		@Override
		public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return task.get(timeout, unit);
		}
	}

}
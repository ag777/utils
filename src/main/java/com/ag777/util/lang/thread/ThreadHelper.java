package com.ag777.util.lang.thread;

import com.ag777.util.lang.interf.Disposable;

import java.util.*;
import java.util.concurrent.*;

/**
 * 多线程工具类(简单封装FutureTask背后的线程管理)
 * 
 * @author ag777
 * Time: created at 2017/3/21. last modify at 2022/05/12.
 */
public class ThreadHelper implements Disposable {

	private ThreadGroup threadGroup;
	private Map<String, FutureTask<?>> taskMap;

	public ThreadHelper(String groupName){
		threadGroup = new ThreadGroup(groupName);
		taskMap = new ConcurrentHashMap<>();
	}

	/*==============添加单个任务==============*/

	/**
	 * 添加任务, 同时会在线程中开始执行任务
	 * @param name 任务名,用于构造线程名, 同时作为获取任务结果的钥匙
	 * @param callable 任务实现
	 * @return ThreadHelper
	 */
	public <T>FutureTask<T> addTask(String name, Callable<T> callable) {
		FutureTask<T> task = new FutureTask<>(callable);
		taskMap.put(name, task);
		Thread t = new Thread(threadGroup, task,threadGroup+"_"+name);
		t.start();
		return task;
	}

	/**
	 * @return 任务map
	 */
	public Map<String, FutureTask<?>> getTaskMap() {
		return taskMap;
	}


	/**
	 *
	 * @param name 钥匙
	 * @param <T> T
	 * @return 任务执行结果
	 * @throws IllegalArgumentException 任务不存在
	 * @throws ExecutionException 任务执行异常
	 * @throws InterruptedException 任务执行中断
	 */
	public <T>T getResult(String name) throws IllegalArgumentException, ExecutionException, InterruptedException {
		FutureTask<T> task = (FutureTask<T>) taskMap.get(name);
		if (task == null) {
			throw new IllegalArgumentException("不存在的token: "+name);
		}
		return task.get();
	}

	/**
	 * 中断正在执行的任务,销毁对象
	 */
	@Override
	public void dispose() {
		if (threadGroup != null) {
			threadGroup.interrupt();
			threadGroup = null;
			taskMap = null;
		}

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ThreadHelper t = new ThreadHelper("my-tg");
		try {
			FutureTask<String> task1 = t.addTask("t1", () -> {
				TimeUnit.SECONDS.sleep(1);
				return "返回数据1";
			});
			FutureTask<Integer> task2 = t.addTask("t2", () -> 2);
			FutureTask<Integer> task3 = t.addTask("t3", ()-> {
				throw new Exception("aaa");
			});
			System.out.println(task1.get());
			System.out.println(task2.get());

			t.addTask("t4", ()->4L);
			long r4 = t.getResult("t4");
			System.out.println(r4);

			System.out.println(task3.get());	// 抛出异常
		} finally {
			t.dispose();
		}
	}


}

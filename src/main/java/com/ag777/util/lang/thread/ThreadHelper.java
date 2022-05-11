package com.ag777.util.lang.thread;

import java.util.*;
import java.util.concurrent.*;

/**
 * 多线程工具类(只辅助,外部靠内存共享数据实现结果处理)
 * 
 * @author ag777
 * Time: created at 2017/3/21. last modify at 2022/05/11.
 */
public class ThreadHelper<T> {

	private ThreadGroup threadGroup;
	private List<Thread> threadList;
	private Map<String, FutureTask<T>> result;
	private volatile boolean isFinish;

	public ThreadHelper(String groupName){
		threadList = new ArrayList<>();
		threadGroup = new ThreadGroup(groupName);
		result = new ConcurrentHashMap<>();
		isFinish = false;
	}

	/*==============添加单个任务==============*/

	public ThreadHelper<T> addTask(String token, Callable<T> callable) {
		FutureTask<T> task = new FutureTask<>(callable);
		result.put(token, task);
		Thread t = new Thread(threadGroup, task,threadGroup+"_"+token);
		threadList.add(t);
		t.start();
		return this;
	}


	public Map<String, FutureTask<T>> getResult() throws InterruptedException {
		if (!isFinish) {
			join();
		}
		return result;
	}


	private ThreadHelper<T> join() throws InterruptedException {
		for (Thread t : threadList) {
			t.join();
		}
		isFinish = true;
		return this;
	}


	/*===静态方法===*/
	public static void startThread(final Callable<Void> t) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					t.call();
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}).start();
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ThreadHelper<String> t = new ThreadHelper<>("my-tg");
		t.addTask("t1", () -> {
			TimeUnit.SECONDS.sleep(1);
			return "返回数据1";
		});
		t.addTask("t2", () ->  "返回数据2");
		Map<String, FutureTask<String>> result = t.getResult();
		for (String token : result.keySet()) {
			System.out.println(token+": "+result.get(token).get());
		}
	}
}

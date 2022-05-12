package com.ag777.util.lang.thread;

import com.ag777.util.lang.interf.Disposable;

import java.util.*;
import java.util.concurrent.*;

/**
 * 多线程工具类(只辅助,外部靠内存共享数据实现结果处理)
 * 
 * @author ag777
 * Time: created at 2017/3/21. last modify at 2022/05/12.
 */
public class ThreadHelper implements Disposable {

	private ThreadGroup threadGroup;

	public ThreadHelper(String groupName){
		threadGroup = new ThreadGroup(groupName);
	}

	/*==============添加单个任务==============*/

	/**
	 * 添加任务, 同时会在线程中开始执行任务
	 * @param name 任务名,用于构造线程名
	 * @param callable 任务实现
	 * @return ThreadHelper
	 */
	public <T>FutureTask<T> addTask(String name, Callable<T> callable) {
		FutureTask<T> task = new FutureTask<>(callable);
		Thread t = new Thread(threadGroup, task,threadGroup+"_"+name);
		t.start();
		return task;
	}

	@Override
	public void dispose() {
		if (threadGroup != null) {
			threadGroup.interrupt();
			threadGroup = null;
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
			System.out.println(task3.get());
		} finally {
			t.dispose();
		}
	}


}

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
public class ThreadHelper<T> implements Disposable {

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

	/**
	 * 添加任务
	 * @param token 用于获取结果的钥匙
	 * @param callable 任务实现
	 * @return ThreadHelper
	 */
	public ThreadHelper<T> addTask(String token, Callable<T> callable) {
		FutureTask<T> task = new FutureTask<>(callable);
		result.put(token, task);
		Thread t = new Thread(threadGroup, task,threadGroup+"_"+token);
		threadList.add(t);
		t.start();
		return this;
	}

	/**
	 *
	 * @return {token: FutureTask}
	 * @throws InterruptedException 线程中断
	 */
	public Map<String, FutureTask<T>> getResult() throws InterruptedException {
		if (!isFinish) {
			join();
		}
		return result;
	}

	/**
	 *
	 * @param token 钥匙
	 * @return 任务执行结果
	 * @throws IllegalArgumentException 任务不存在
	 * @throws InterruptedException 线程中断
	 * @throws ExecutionException 任务执行异常
	 */
	public T getResult(String token) throws IllegalArgumentException, InterruptedException, ExecutionException {
		if (!isFinish) {
			join();
		}
		FutureTask<T> task = result.get(token);
		if (task == null) {
			throw new IllegalArgumentException("不存在的token");
		}
		return task.get();
	}


	/**
	 * 等待所有线程执行完成
	 * @return ThreadHelper
	 * @throws InterruptedException 线程中断
	 */
	private synchronized ThreadHelper<T> join() throws InterruptedException {
		Iterator<Thread> itor = threadList.iterator();
		while (itor.hasNext()) {
			itor.next().join();
			itor.remove();
		}
		isFinish = true;
		return this;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ThreadHelper<String> t = new ThreadHelper<>("my-tg");
		t.addTask("t1", () -> {
			TimeUnit.SECONDS.sleep(1);
			return "返回数据1";
		});
		t.addTask("t2", () ->  "返回数据2");
		t.addTask("t3", ()-> {
			throw new Exception("aaa");
		});
		System.out.println(t.getResult("t2"));
		Map<String, FutureTask<String>> result = t.getResult();
		for (String token : result.keySet()) {
			System.out.println(token+": "+result.get(token).get());
		}
	}

	@Override
	public void dispose() {
		if (threadGroup != null && !isFinish) {
			threadGroup.interrupt();
			threadGroup = null;
			threadList = null;
			result = null;
		}

	}
}

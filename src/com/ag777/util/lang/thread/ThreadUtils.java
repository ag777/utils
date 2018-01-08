package com.ag777.util.lang.thread;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 有关<code>Thread</code>的工具类。
 * 
 * @author ag777
 * @version create on 2018年01月08日,last modify at 2017年01月08日
 */
public class ThreadUtils {

	private ThreadUtils() {}
	
	/**
	 * 创建一个可以获取返回值的子线程并返回FutureTask，外部可以通过get()方法来挂起父线程来获取返回
	 * @param callable
	 * @return
	 */
	public static <T>FutureTask<T> task(Callable<T> callable) {
		FutureTask<T> task = new FutureTask<T>(callable);
		new Thread(task).start();
		return task;
	}
	
	public static <T>Optional<T> taskForResult(Callable<T> callable) {
		try {
			return Optional.ofNullable(task(callable).get());
		} catch (InterruptedException|ExecutionException e) {
//			e.printStackTrace();
		}
		return Optional.empty();
	}
}

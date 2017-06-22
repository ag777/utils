package com.ag777.util.lang.thread;

import java.util.List;
import java.util.concurrent.Callable;

public interface TaskHelperInterf<T> {

	TaskHelperInterf<T> addTask(Callable<T> callable) throws Exception;
	TaskHelperInterf<T> addTasks(List<Callable<T>> callables) throws Exception;
	List<T> getResult() throws Exception;
}
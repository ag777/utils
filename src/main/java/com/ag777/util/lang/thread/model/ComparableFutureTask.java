package com.ag777.util.lang.thread.model;

import java.util.concurrent.FutureTask;

/**
 * 可对比的FutureTask,用于优先队列线程池执行callable报错的问题
 * @author ag777 <837915770@vip.qq.com>
 * Time: created at 2023/3/1. last modify at 2023/3/1.
 */
public class ComparableFutureTask<E,T extends ComparableTask<E, T>> extends FutureTask<E> implements Comparable<ComparableFutureTask<E, T>>{

    private T task;

    public T getTask() {
        return task;
    }

    public ComparableFutureTask(T task) {
        super(task);
        this.task = task;
    }

    @Override
    public int compareTo(ComparableFutureTask<E, T> nextTask) {
        if (nextTask == null || nextTask.getTask() == null) {
            return 1;
        }
        return task.compareTo(nextTask.getTask());

    }
}
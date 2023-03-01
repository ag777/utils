package com.ag777.util.lang.thread;

import com.ag777.util.lang.thread.model.ComparableFutureTask;
import com.ag777.util.lang.thread.model.ComparableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 * @author ag777 <837915770@vip.qq.com>
 * Time: created at 2023/3/1. last modify at 2023/3/1.
 */
public class ThreadPoolUtils {

    /**
     * 等待子线程都结束
     * <p>
     * 	调用此方法后线程池不再接受新的任务,之后每100毫秒检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
     * </p>
     * @param pool 线程池
     * @throws InterruptedException InterruptedException
     */
    public static void waitFor(ExecutorService pool) throws InterruptedException {
        waitFor(pool, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 等待子线程都结束
     * <p>
     * 	调用此方法后线程池不再接受新的任务,之后每根据参数指定的时间间隔检查一次子线程是否都完成（阻塞当前线程）,如果任务均完成则可以继续执行后续代码
     * </p>
     * @param pool 线程池
     * @param timeout timeout
     * @param unit unit
     * @throws InterruptedException InterruptedException
     */
    public static void waitFor(ExecutorService pool, long timeout, TimeUnit unit) throws InterruptedException {
        pool.shutdown();
        while(!pool.awaitTermination(timeout, unit)) {	//如果结束则关闭线程池
        }
    }

    /**
     * 包含PriorityBlockingQueue的线程池通过submit提交callable,报错
     * {@code FutureTask cannot be cast to java.lang.Comparable},
     * 针对这种情况，手动封装callable解决问题
     * @param pool 线程池,包含优先队列，如:PriorityBlockingQueue
     * @param task 异步带返回的任务，同时实现Callable和Comparable
     * @param <E> 任务返回类型
     * @param <T> 任务类本身类型
     * @return 异步任务,实际是ComparableFutureTask
     */
    public static <E, T extends ComparableTask<E, T>>FutureTask<E> executePriority(ThreadPoolExecutor pool, T task) {
        FutureTask<E> t = new ComparableFutureTask<>(task);
        pool.execute(t);
        return t;
    }

    private ThreadPoolUtils() {}
}

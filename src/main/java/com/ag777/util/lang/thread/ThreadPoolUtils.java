package com.ag777.util.lang.thread;

import com.ag777.util.lang.Console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ag777 <837915770@vip.qq.com>
 * @Description 线程池工具类
 * @Date 2021/6/7 14:58
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

    private ThreadPoolUtils() {}
}

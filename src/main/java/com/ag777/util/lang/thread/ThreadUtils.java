package com.ag777.util.lang.thread;

/**
 * 线程工具类
 * @author ag777 <837915770@vip.qq.com>
 * @version 2023/5/6 9:03
 */
public class ThreadUtils {

    private ThreadUtils() {}

    /**
     *
     * @return 调用者的方法名
     */
    public static String getCurrentMethodName() {
        return getCurrentStack().getMethodName();
    }

    /**
     * @return 调用者所在堆栈的项
     */
    private static StackTraceElement getCurrentStack() {
        return Thread.currentThread().getStackTrace()[3];
    }

    /**
     * 判断当前线程是否中断
     * @throws InterruptedException 中断异常
     */
    public static void checkInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }
}
